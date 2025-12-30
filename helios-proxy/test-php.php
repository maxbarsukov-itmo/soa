<?php
header('Content-type: text/plain');

echo "PHP Version: " . PHP_VERSION . "\n";
echo "allow_url_fopen: " . (ini_get('allow_url_fopen') ? 'On' : 'Off') . "\n";
echo "memory_limit: " . ini_get('memory_limit') . "\n";
echo "max_execution_time: " . ini_get('max_execution_time') . "\n";
?>
