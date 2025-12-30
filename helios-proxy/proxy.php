<?php
header('Content-Type: text/xml; charset=utf-8');

error_log("[" . date('Y-m-d H:i:s') . "] UDDI Proxy called");

$TARGET_URL = 'http://127.0.0.1:15613/uddi';

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    if (isset($_GET['wsdl']) || strpos($_SERVER['REQUEST_URI'], '.wsdl') !== false) {
        error_log("WSDL request");

        $wsdl = @file_get_contents('http://127.0.0.1:15613/uddi/uddiapi.wsdl');
        if ($wsdl === false) {
            error_log("Failed to fetch WSDL");
            sendSoapFault("Cannot fetch WSDL");
        }

        $proxyUrl = (isset($_SERVER['HTTPS']) ? 'https' : 'http')
                  . '://' . $_SERVER['HTTP_HOST']
                  . dirname($_SERVER['REQUEST_URI'])
                  . '/proxy.php';

        $wsdl = str_replace('http://127.0.0.1:15613/uddi', $proxyUrl, $wsdl);

        echo $wsdl;
        error_log("WSDL served");
        exit;
    }

    header('Content-Type: text/plain');
    echo 'UDDI Proxy is running. Use ?wsdl for WSDL.';
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    error_log("SOAP request started");

    $input = file_get_contents('php://input');
    if (empty($input)) {
        error_log("Empty request body");
        sendSoapFault("Empty SOAP request");
    }

    error_log("Request size: " . strlen($input) . " bytes");

    $soapAction = '';
    foreach ($_SERVER as $key => $value) {
        if ($key === 'HTTP_SOAPACTION') {
            $soapAction = $value;
            break;
        }
    }

    error_log("SOAPAction: " . $soapAction);

    $headers = [
        'Content-Type: text/xml; charset=utf-8',
        'User-Agent: UDDI-PHP-Proxy/1.0',
        'X-Forwarded-For: ' . ($_SERVER['REMOTE_ADDR'] ?? 'unknown'),
        'X-Forwarded-Host: ' . $_SERVER['HTTP_HOST'],
    ];

    if (!empty($soapAction)) {
        $headers[] = 'SOAPAction: ' . $soapAction;
    }

    if (isset($_SERVER['HTTP_ACCEPT'])) {
        $headers[] = 'Accept: ' . $_SERVER['HTTP_ACCEPT'];
    }

    if (isset($_SERVER['HTTP_ACCEPT_ENCODING'])) {
        $headers[] = 'Accept-Encoding: ' . $_SERVER['HTTP_ACCEPT_ENCODING'];
    }

    error_log("Request headers: " . implode(", ", $headers));

    $contextOptions = [
        'http' => [
            'method' => 'POST',
            'header' => implode("\r\n", $headers),
            'content' => $input,
            'timeout' => 30,
            'ignore_errors' => true,
        ],
        'ssl' => [
            'verify_peer' => false,
            'verify_peer_name' => false,
        ]
    ];

    $context = stream_context_create($contextOptions);

    error_log("Sending request to: $TARGET_URL");

    $response = @file_get_contents($TARGET_URL, false, $context);

    if ($response === false) {
        $error = error_get_last();
        error_log("Request failed: " . ($error['message'] ?? 'Unknown error'));
        sendSoapFault("Failed to connect to UDDI service: " . ($error['message'] ?? 'Unknown error'));
    }

    if (isset($http_response_header)) {
        error_log("Got response, headers count: " . count($http_response_header));

        foreach ($http_response_header as $header) {
            $header = trim($header);

            if (preg_match('/^HTTP\/\d\.\d\s+(\d+)/', $header, $matches)) {
                http_response_code($matches[1]);
                error_log("HTTP Status: " . $matches[1]);
                continue;
            }

            if (empty($header)) {
                continue;
            }

            if (preg_match('/^([^:]+):\s*(.+)$/', $header, $matches)) {
                $name = trim($matches[1]);
                $value = trim($matches[2]);

                $skipHeaders = ['transfer-encoding', 'connection', 'keep-alive'];
                if (!in_array(strtolower($name), $skipHeaders)) {
                    header("$name: $value", false);
                    error_log("Forwarding header: $name: $value");
                }
            }
        }
    }

    error_log("Response size: " . strlen($response) . " bytes");
    error_log("Response first 200 chars: " . substr($response, 0, 200));

    echo $response;
    error_log("Request completed successfully");
    exit;
}

http_response_code(405);
echo 'Method Not Allowed';

function sendSoapFault($message) {
    $xml = '<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <soap:Fault>
      <faultcode>soap:Server</faultcode>
      <faultstring>' . htmlspecialchars($message) . '</faultstring>
    </soap:Fault>
  </soap:Body>
</soap:Envelope>';

    http_response_code(500);
    echo $xml;
    exit;
}
?>
