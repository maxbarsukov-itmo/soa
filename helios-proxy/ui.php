<?php
?>

<!DOCTYPE html>
<html lang="ru">
<head>
    <title>UDDI Proxy Test</title>
    <meta charset="UTF-8">
    <link rel='shortcut icon' type='image/x-icon' href='/~s367081/favicon.ico' />
    <style>
        :root {
            --primary-color: #007bff;
            --primary-dark: #0056b3;
            --secondary-color: #6c757d;
            --success-color: #28a745;
            --danger-color: #dc3545;
            --light-bg: #f8f9fa;
            --dark-bg: #343a40;
            --border-color: #dee2e6;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 15px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
        }

        .header {
            background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
            color: white;
            padding: 30px;
            text-align: center;
        }

        .header h1 {
            margin: 0;
            font-size: 2.5rem;
            font-weight: 300;
        }

        .header p {
            margin: 10px 0 0;
            opacity: 0.9;
            font-size: 1.1rem;
        }

        .tabs {
            display: flex;
            background: var(--light-bg);
            border-bottom: 1px solid var(--border-color);
        }

        .tab {
            flex: 1;
            text-align: center;
            padding: 15px;
            cursor: pointer;
            border: none;
            background: none;
            font-size: 16px;
            color: var(--secondary-color);
            transition: all 0.3s ease;
            position: relative;
        }

        .tab:hover {
            background: rgba(0,123,255,0.1);
            color: var(--primary-color);
        }

        .tab.active {
            color: var(--primary-color);
            font-weight: 500;
        }

        .tab.active::after {
            content: '';
            position: absolute;
            bottom: -1px;
            left: 0;
            right: 0;
            height: 3px;
            background: var(--primary-color);
        }

        .tab-content {
            padding: 30px;
            display: none;
            animation: fadeIn 0.3s ease;
        }

        .tab-content.active {
            display: block;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .section {
            margin-bottom: 30px;
        }

        .section h3 {
            color: var(--dark-bg);
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 2px solid var(--border-color);
        }

        .btn-group {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            margin-bottom: 20px;
        }

        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.2s ease;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }

        .btn-primary {
            background: var(--primary-color);
            color: white;
        }

        .btn-primary:hover {
            background: var(--primary-dark);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,123,255,0.4);
        }

        .btn-secondary {
            background: var(--secondary-color);
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
        }

        .btn-success {
            background: var(--success-color);
            color: white;
        }

        .btn-success:hover {
            background: #218838;
        }

        textarea {
            width: 100%;
            height: 300px;
            font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
            font-size: 13px;
            padding: 15px;
            border: 2px solid var(--border-color);
            border-radius: 8px;
            resize: vertical;
            background: #fafafa;
            transition: border-color 0.3s ease;
        }

        textarea:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(0,123,255,0.25);
        }

        .result {
            margin-top: 30px;
            padding: 20px;
            background: var(--light-bg);
            border-radius: 10px;
            border-left: 5px solid var(--primary-color);
        }

        .result h3 {
            margin-top: 0;
            color: var(--primary-color);
        }

        pre {
            background: #1e1e1e;
            color: #d4d4d4;
            padding: 20px;
            border-radius: 8px;
            overflow-x: auto;
            font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
            font-size: 13px;
            line-height: 1.5;
            margin: 0;
            max-height: 500px;
            overflow-y: auto;
        }

        .status-badge {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            margin-left: 10px;
        }

        .status-success {
            background: #d4edda;
            color: #155724;
        }

        .status-error {
            background: #f8d7da;
            color: #721c24;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .info-card {
            background: white;
            border: 1px solid var(--border-color);
            border-radius: 10px;
            padding: 20px;
            transition: transform 0.3s ease;
        }

        .info-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }

        .info-card h4 {
            margin-top: 0;
            color: var(--primary-color);
        }

        .actions-list {
            list-style: none;
            padding: 0;
            margin: 0;
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 10px;
        }

        .actions-list li {
            background: var(--light-bg);
            padding: 8px 12px;
            border-radius: 6px;
            font-size: 14px;
        }

        .endpoints {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
            margin: 20px 0;
        }

        .endpoint {
            display: inline-block;
            padding: 10px 20px;
            background: var(--primary-color);
            color: white;
            text-decoration: none;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .endpoint:hover {
            background: var(--primary-dark);
            transform: translateY(-2px);
        }

        .response-info {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid var(--border-color);
        }

        .loader {
            display: none;
            text-align: center;
            padding: 40px;
        }

        .loader.active {
            display: block;
        }

        .spinner {
            border: 4px solid var(--border-color);
            border-top: 4px solid var(--primary-color);
            border-radius: 50%;
            width: 40px;
            height: 40px;
            animation: spin 1s linear infinite;
            margin: 0 auto 20px;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        .xml-tag { color: #569cd6; }
        .xml-attr { color: #9cdcfe; }
        .xml-value { color: #ce9178; }
        .xml-comment { color: #6a9955; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🔗 UDDI SOAP Proxy Test</h1>
            <p>Тестирование прокси для Java UDDI сервиса (порт 15613)</p>
        </div>

        <div class="tabs">
            <button class="tab active" onclick="showTab(0)">📡 Тест запросов</button>
            <button class="tab" onclick="showTab(1)">📄 WSDL информация</button>
            <button class="tab" onclick="showTab(2)">🖥 Команды cURL</button>
            <button class="tab" onclick="showTab(3)">⚙ Настройки</button>
        </div>

        <div class="tab-content active" id="tab-0">
            <div class="section">
                <h3>Предустановленные запросы:</h3>
                <div class="btn-group">
                    <button class="btn btn-primary" onclick="testRequest('find_business')">
                        <span>🔍</span> find_business
                    </button>
                    <button class="btn btn-primary" onclick="testRequest('get_authToken')">
                        <span>🔑</span> get_authToken
                    </button>
                    <button class="btn btn-primary" onclick="testRequest('save_business')">
                        <span>💾</span> save_business
                    </button>
                    <button class="btn btn-secondary" onclick="testRequest('find_service')">
                        <span>🛠</span> find_service
                    </button>
                    <button class="btn btn-secondary" onclick="testRequest('save_service')">
                        <span>📁</span> save_service
                    </button>
                </div>
            </div>

            <div class="section">
                <h3>Пользовательский SOAP запрос:</h3>
                <textarea id="customRequest" placeholder="Вставьте или напишите SOAP XML запрос..."><?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:uddi="urn:uddi-org:api_v3">
  <soap:Header/>
  <soap:Body>
    <uddi:find_business>
      <uddi:name>Test Company</uddi:name>
      <uddi:maxRows>10</uddi:maxRows>
    </uddi:find_business>
  </soap:Body>
</soap:Envelope></textarea>
                <div style="margin-top: 15px;">
                    <button class="btn btn-success" onclick="sendCustomRequest()">
                        <span>🚀</span> Отправить запрос
                    </button>
                    <button class="btn btn-secondary" onclick="formatXML()">
                        <span>✨</span> Форматировать XML
                    </button>
                    <button class="btn btn-secondary" onclick="clearRequest()">
                        <span>🗑</span> Очистить
                    </button>
                </div>
            </div>

            <div class="loader" id="loader">
                <div class="spinner"></div>
                <p>Отправка запроса к UDDI сервису...</p>
            </div>

            <div class="result" id="result">
                <h3>Ответ появится здесь</h3>
                <p>Выберите запрос и нажмите "Отправить запрос"</p>
            </div>
        </div>

        <div class="tab-content" id="tab-1">
            <div class="section">
                <h3>Доступные endpoint-ы:</h3>
                <div class="endpoints">
                    <a href="proxy.php?wsdl" target="_blank" class="endpoint">🌐 WSDL через прокси</a>
                    <a href="http://127.0.0.1:15613/uddi/uddiapi.wsdl" target="_blank" class="endpoint">⚙ Оригинальный WSDL</a>
                </div>
            </div>

            <div class="section">
                <h3>Информация о прокси:</h3>
                <div class="info-grid">
                    <div class="info-card">
                        <h4>URL прокси:</h4>
                        <pre style="background: #f8f9fa; color: #333; padding: 10px; border-radius: 5px;"><?php
                            $proxyUrl = (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on' ? 'https' : 'http')
                                      . '://' . $_SERVER['HTTP_HOST'] . dirname($_SERVER['REQUEST_URI']);
                            echo $proxyUrl . '/proxy.php';
                        ?></pre>
                    </div>

                    <div class="info-card">
                        <h4>Java сервис:</h4>
                        <p><strong>Адрес:</strong> 127.0.0.1:15613</p>
                        <p><strong>Статус:</strong> <span id="serviceStatus">Проверка...</span></p>
                    </div>
                </div>
            </div>

            <div class="section">
                <h3>Поддерживаемые SOAP действия:</h3>
                <ul class="actions-list">
                    <li>✅ find_business</li>
                    <li>✅ get_businessDetail</li>
                    <li>✅ find_service</li>
                    <li>✅ get_serviceDetail</li>
                    <li>✅ find_binding</li>
                    <li>✅ get_bindingDetail</li>
                    <li>✅ find_tModel</li>
                    <li>✅ get_tModelDetail</li>
                    <li>✅ get_authToken</li>
                    <li>✅ save_business</li>
                    <li>✅ save_service</li>
                    <li>✅ save_binding</li>
                    <li>✅ save_tModel</li>
                    <li>✅ delete_business</li>
                    <li>✅ delete_service</li>
                </ul>
            </div>
        </div>

        <div class="tab-content" id="tab-2">
            <div class="section">
                <h3>Тестирование через командную строку:</h3>
                <p>Выполните эту команду в терминале:</p>
                <pre style="background: #f0f0f0; color: #333;">
curl -X POST \
  "<?php echo $proxyUrl . '/proxy.php'; ?>" \
  -H "Content-Type: text/xml; charset=utf-8" \
  -H "SOAPAction: \"urn:uddi-org:api_v3#find_business\"" \
  -d '&lt;?xml version="1.0" encoding="UTF-8"?&gt;
&lt;soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:uddi="urn:uddi-org:api_v3"&gt;
  &lt;soap:Header/&gt;
  &lt;soap:Body&gt;
    &lt;uddi:find_business&gt;
      &lt;uddi:name&gt;Test Company&lt;/uddi:name&gt;
      &lt;uddi:maxRows&gt;10&lt;/uddi:maxRows&gt;
    &lt;/uddi:find_business&gt;
  &lt;/soap:Body&gt;
&lt;/soap:Envelope&gt;'</pre>
            </div>

            <div class="section">
                <h3>Примеры команд:</h3>
                <div class="info-grid">
                    <div class="info-card">
                        <h4>get_authToken:</h4>
                        <pre style="font-size: 11px; max-height: 200px;">
curl -X POST "<?php echo $proxyUrl . '/proxy.php'; ?>" \
  -H "Content-Type: text/xml; charset=utf-8" \
  -H "SOAPAction: \"urn:uddi-org:api_v3#get_authToken\"" \
  -d '&lt;soap:Envelope xmlns:soap=&quot;http://schemas.xmlsoap.org/soap/envelope/&quot;
               xmlns:uddi=&quot;urn:uddi-org:api_v3&quot;&gt;
  &lt;soap:Body&gt;
    &lt;get_authToken xmlns=&quot;urn:uddi-org:api_v3&quot; userID=&quot;testUser&quot; cred=&quot;ignored&quot;/&gt;
  &lt;/soap:Body&gt;
&lt;/soap:Envelope&gt;'</pre>
                    </div>

                    <div class="info-card">
                        <h4>save_business:</h4>
                        <pre style="font-size: 11px; max-height: 200px;">
curl -X POST "<?php echo $proxyUrl . '/proxy.php'; ?>" \
  -H "Content-Type: text/xml; charset=utf-8" \
  -H "SOAPAction: \"urn:uddi-org:api_v3#save_business\"" \
  -d '&lt;?xml version="1.0"?&gt;
&lt;soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:uddi="urn:uddi-org:api_v3"&gt;
  &lt;soap:Body&gt;
    &lt;uddi:save_business&gt;
      &lt;uddi:authInfo&gt;YOUR_TOKEN&lt;/uddi:authInfo&gt;
      &lt;uddi:businessEntity&gt;
        &lt;uddi:name&gt;New Company&lt;/uddi:name&gt;
      &lt;/uddi:businessEntity&gt;
    &lt;/uddi:save_business&gt;
  &lt;/soap:Body&gt;
&lt;/soap:Envelope&gt;'</pre>
                    </div>
                </div>
            </div>
        </div>

        <div class="tab-content" id="tab-3">
            <div class="section">
                <h3>Настройки сервера:</h3>
                <div class="info-grid">
                    <div class="info-card">
                        <h4>PHP информация:</h4>
                        <p><strong>Версия PHP:</strong> <?php echo PHP_VERSION; ?></p>
                        <p><strong>allow_url_fopen:</strong> <?php echo ini_get('allow_url_fopen') ? '✅ Включено' : '❌ Выключено'; ?></p>
                        <p><strong>memory_limit:</strong> <?php echo ini_get('memory_limit'); ?></p>
                    </div>

                    <div class="info-card">
                        <h4>Пути:</h4>
                        <p><strong>Текущий путь:</strong> <?php echo __DIR__; ?></p>
                    </div>
                </div>
            </div>

            <div class="section">
                <h3>Диагностика:</h3>
                <div class="btn-group">
                    <button class="btn btn-secondary" onclick="testConnection()">
                        <span>🔗</span> Проверить соединение
                    </button>
                </div>
                <div id="diagnosticResult" style="margin-top: 20px;"></div>
            </div>
        </div>
    </div>

    <script>
        const requests = {
            'find_business': `<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:uddi="urn:uddi-org:api_v3">
  <soap:Header/>
  <soap:Body>
    <uddi:find_business>
      <uddi:name>Test Company</uddi:name>
      <uddi:maxRows>10</uddi:maxRows>
    </uddi:find_business>
  </soap:Body>
</soap:Envelope>`,

            'get_authToken': `<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:uddi="urn:uddi-org:api_v3">
  <soap:Body>
    <get_authToken xmlns="urn:uddi-org:api_v3" userID="testUser" cred="ignored"/>
  </soap:Body>
</soap:Envelope>`,

            'save_business': `<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
  <soapenv:Body>
    <save_business xmlns="urn:uddi-org:api_v3">
      <authInfo>{{authToken}}</authInfo>
      <businessEntity>
        <name>Test Business</name>
        <description>Business for UDDI testing</description>
        <discoveryURLs>
          <discoveryURL useType="home">https://testbusiness.example.com</discoveryURL>
        </discoveryURLs>
        <contacts>
          <contact useType="admin">
            <personName>Test Admin</personName>
            <phone>+71234567890</phone>
            <email>admin@testbusiness.example.com</email>
          </contact>
        </contacts>
      </businessEntity>
    </save_business>
  </soapenv:Body>
</soapenv:Envelope>
`,

            'find_service': `<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:uddi="urn:uddi-org:api_v3">
  <soap:Header/>
  <soap:Body>
    <uddi:find_service>
      <uddi:name>Test Service</uddi:name>
      <uddi:maxRows>10</uddi:maxRows>
    </uddi:find_service>
  </soap:Body>
</soap:Envelope>`,

            'save_service': `<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:uddi="urn:uddi-org:api_v3">
  <soap:Header/>
  <soap:Body>
    <uddi:save_service>
      <uddi:authInfo>test-auth-token</uddi:authInfo>
      <uddi:businessService>
        <uddi:name>New Service</uddi:name>
        <uddi:businessKey>uuid:test-business-key</uddi:businessKey>
      </uddi:businessService>
    </uddi:save_service>
  </soap:Body>
</soap:Envelope>`
        };

        const soapActions = {
            'find_business': 'urn:uddi-org:api_v3#find_business',
            'get_authToken': 'urn:uddi-org:api_v3#get_authToken',
            'save_business': 'urn:uddi-org:api_v3#save_business',
            'find_service': 'urn:uddi-org:api_v3#find_service',
            'save_service': 'urn:uddi-org:api_v3#save_service'
        };

        function showTab(tabIndex) {
            document.querySelectorAll('.tab').forEach((tab, idx) => {
                tab.classList.toggle('active', idx === tabIndex);
            });
            document.querySelectorAll('.tab-content').forEach((content, idx) => {
                content.classList.toggle('active', idx === tabIndex);
            });
        }

        function testRequest(type) {
            document.getElementById('customRequest').value = requests[type];
            sendCustomRequest();
        }

        function sendCustomRequest() {
            const soapRequest = document.getElementById('customRequest').value;
            const resultDiv = document.getElementById('result');
            const loader = document.getElementById('loader');

            let requestType = 'find_business';
            for (const [type, xml] of Object.entries(requests)) {
                if (soapRequest.includes(`<uddi:${type}>`)) {
                    requestType = type;
                    break;
                }
            }

            const soapAction = soapActions[requestType] || 'urn:uddi-org:api_v3#find_business';

            loader.classList.add('active');
            resultDiv.innerHTML = '';

            fetch('proxy.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'text/xml; charset=utf-8',
                    'SOAPAction': soapAction
                },
                body: soapRequest
            })
            .then(response => {
                const status = response.status;
                const statusClass = status >= 200 && status < 300 ? 'status-success' : 'status-error';

                return response.text().then(text => ({
                    status,
                    text,
                    statusClass
                }));
            })
            .then(({ status, text, statusClass }) => {
                loader.classList.remove('active');

                const coloredXML = syntaxHighlightXML(text);
                const isSuccess = status >= 200 && status < 300;

                resultDiv.innerHTML = `
                    <div class="response-info">
                        <h3>Ответ от сервера</h3>
                        <span class="status-badge ${statusClass}">
                            HTTP ${status} ${isSuccess ? 'Успешно' : 'Ошибка'}
                        </span>
                    </div>
                    <p><strong>Размер ответа:</strong> ${text.length} байт</p>
                    <p><strong>Тип запроса:</strong> ${requestType}</p>
                    ${coloredXML}
                `;

                resultDiv.scrollIntoView({ behavior: 'smooth' });
            })
            .catch(error => {
                loader.classList.remove('active');
                resultDiv.innerHTML = `
                    <h3>Ошибка соединения</h3>
                    <pre>${error}</pre>
                    <p>Возможные причины:
                    <ul>
                        <li>Java UDDI сервис не запущен на порту 15613</li>
                        <li>Прокси скрипт имеет недостаточные права доступа</li>
                        <li>Межсетевой экран блокирует соединение</li>
                        <li>Проблемы с конфигурацией PHP (allow_url_fopen)</li>
                    </ul>
                    </p>
                `;
            });
        }

        function formatXML() {
            const textarea = document.getElementById('customRequest');
            try {
                const xmlString = textarea.value;
                const formatted = formatXml(xmlString);
                textarea.value = formatted;
            } catch (e) {
                alert('Ошибка форматирования XML: ' + e.message);
            }
        }

        function formatXml(xml) {
            const PADDING = '  ';
            const reg = /(>)(<)(\/*)/g;
            let formatted = '';
            let pad = 0;

            xml = xml.replace(reg, '$1\r\n$2$3');

            xml.split('\r\n').forEach((node) => {
                let indent = 0;
                if (node.match(/.+<\/\w[^>]*>$/)) {
                    indent = 0;
                } else if (node.match(/^<\/\w/)) {
                    if (pad !== 0) pad -= 1;
                } else if (node.match(/^<\w[^>]*[^/]>.*$/)) {
                    indent = 1;
                } else {
                    indent = 0;
                }

                formatted += PADDING.repeat(pad) + node + '\r\n';
                pad += indent;
            });

            return formatted.trim();
        }

        function syntaxHighlightXML(xml) {
            if (!xml.trim()) return '<pre>Пустой ответ</pre>';

            // xml = xml.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
            // xml = xml.replace(/(&lt;\/*?\w+)/g, '<span class="xml-tag">$1</span>');
            // xml = xml.replace(/(\w+=)/g, '<span class="xml-attr">$1</span>');
            // xml = xml.replace(/(".*?")/g, '<span class="xml-value">$1</span>');
            xml = xml.replace(/&lt;!--.*?--&gt;/g, '<span class="xml-comment">$&</span>');

            return `<pre lang="xml"><xmp>${xml.substring(0, 1) == "<" ? formatXml(xml) : xml}</xmp></pre>`;
        }

        function clearRequest() {
            if (confirm('Очистить поле запроса?')) {
                document.getElementById('customRequest').value = '';
            }
        }

        function checkServiceStatus() {
            const statusElement = document.getElementById('serviceStatus');
            statusElement.textContent = 'Проверка...';

            fetch('proxy.php?wsdl', { method: 'GET' })
                .then(response => {
                    if (response.ok) {
                        statusElement.textContent = '✅ Доступен';
                        statusElement.style.color = '#28a745';
                    } else {
                        statusElement.textContent = '❌ Недоступен';
                        statusElement.style.color = '#dc3545';
                    }
                })
                .catch(() => {
                    statusElement.textContent = '❌ Ошибка соединения';
                    statusElement.style.color = '#dc3545';
                });
        }

        function testConnection() {
            const resultDiv = document.getElementById('diagnosticResult');
            resultDiv.innerHTML = '<p>Проверка соединения с Java сервисом...</p>';

            fetch('proxy.php?wsdl')
                .then(response => response.text())
                .then(text => {
                    if (text.includes('wsdl:definitions')) {
                        resultDiv.innerHTML = '<p style="color: #28a745;">✅ Соединение установлено. WSDL получен успешно.</p>';
                    } else {
                        resultDiv.innerHTML = '<p style="color: #dc3545;">❌ Получен неожиданный ответ от сервиса.</p>';
                    }
                })
                .catch(error => {
                    resultDiv.innerHTML = `<p style="color: #dc3545;">❌ Ошибка: ${error}</p>`;
                });
        }

        function escapeHtml(text) {
            const div = document.createElement('div');
            div.textContent = text;
            return div.innerHTML;
        }

        document.addEventListener('DOMContentLoaded', function() {
            checkServiceStatus();
        });
    </script>
</body>
</html>
