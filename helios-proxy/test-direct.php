<?php
header('Content-Type: text/plain');

echo "=== Direct Connection Test ===\n\n";

echo "1. Testing port 15613 connection...\n";
$timeout = 5;
$fp = @fsockopen('127.0.0.1', 15613, $errno, $errstr, $timeout);

if ($fp) {
    echo "   ✓ Port 15613 is OPEN\n";
    $request = "GET /uddi/uddiapi.wsdl HTTP/1.1\r\n";
    $request .= "Host: 127.0.0.1:15613\r\n";
    $request .= "Connection: close\r\n\r\n";
    fwrite($fp, $request);
    $response = '';
    while (!feof($fp)) {
        $response .= fgets($fp, 128);
    }
    fclose($fp);
    if (strpos($response, 'HTTP/1.1 200') !== false) {
        echo "   ✓ Got HTTP 200 response\n";
    } else {
        echo "   ✗ Unexpected response\n";
        echo "   Response start:\n" . substr($response, 0, 500) . "\n";
    }
} else {
    echo "   ✗ Port 15613 is CLOSED or blocked\n";
    echo "   Error: $errstr (errno: $errno)\n";
    echo "\nPossible solutions:\n";
    echo "1. Check if Java app is running: ps aux | grep java\n";
    echo "2. Check what's listening on port: netstat -tlnp | grep 15613\n";
    echo "3. Java might be binding to 127.0.0.1 only - check app config\n";
    echo "4. There might be a firewall blocking PHP->localhost\n";
}

echo "\n2. Testing with cURL...\n";
if (function_exists('curl_init')) {
    $ch = curl_init('http://127.0.0.1:15613/uddi/uddiapi.wsdl');
    curl_setopt_array($ch, [
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_TIMEOUT => 10,
        CURLOPT_HEADER => true,
        CURLOPT_NOBODY => false,
    ]);
    $response = curl_exec($ch);
    if ($response) {
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        echo "   ✓ cURL got response, HTTP $httpCode\n";
        if ($httpCode === 200) {
            echo "   ✓ Successfully retrieved WSDL\n";
        }
    } else {
        echo "   ✗ cURL failed: " . curl_error($ch) . "\n";
    }
    curl_close($ch);
} else {
    echo "   ✗ cURL not available\n";
}

echo "\n3. Testing with file_get_contents...\n";
$context = stream_context_create([
    'http' => [
        'timeout' => 10,
        'ignore_errors' => true,
    ]
]);

$content = @file_get_contents('http://127.0.0.1:15613/uddi/uddiapi.wsdl', false, $context);
if ($content !== false) {
    echo "   ✓ file_get_contents succeeded\n";
    echo "   Content size: " . strlen($content) . " bytes\n";
    echo "   First line: " . strtok($content, "\n") . "\n";
} else {
    echo "   ✗ file_get_contents failed\n";
    $error = error_get_last();
    echo "   Error: " . ($error['message'] ?? 'Unknown') . "\n";
}

echo "\n4. Testing SOAP request...\n";
$soapRequest = '<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:uddi="urn:uddi-org:api_v3">
  <soap:Header/>
  <soap:Body>
    <uddi:find_business>
      <uddi:name>Test</uddi:name>
    </uddi:find_business>
  </soap:Body>
</soap:Envelope>';

$context = stream_context_create([
    'http' => [
        'method' => 'POST',
        'header' => "Content-Type: text/xml; charset=utf-8\r\n" .
                   "SOAPAction: \"urn:uddi-org:api_v3#find_business\"\r\n",
        'content' => $soapRequest,
        'timeout' => 10,
        'ignore_errors' => true,
    ]
]);

$response = @file_get_contents('http://127.0.0.1:15613/uddi', false, $context);
if ($response !== false) {
    echo "   ✓ SOAP request succeeded\n";
    echo "   Response size: " . strlen($response) . " bytes\n";
    if (isset($http_response_header[0])) {
        echo "   Response: " . $http_response_header[0] . "\n";
    }
} else {
    echo "   ✗ SOAP request failed\n";
    $error = error_get_last();
    echo "   Error: " . ($error['message'] ?? 'Unknown') . "\n";
}

?>
