<?php
// Contact Form

$config = include('config.php');

if (!isset($config['forwarding_name'])
    or !isset($config['forwarding_email'])) {
    http_response_code(500);
    echo 'Required config fields: forwarding_name, forwarding_email';
    die();
}

// Ensure all required fields are sent
if (!isset($_POST['namsndkjjkba'])
        or !isset($_POST['emaksjkbkjad'])
        or !isset($_POST['enqkjadskbnz'])) {
    http_response_code(400);
    echo 'Required POST fields: name, email, enquiry';
    die("name: " . $_POST['namsndkjjkba'] . ", email: " . $_POST['emaksjkbkjad'] . ", enquiry: " . $_POST['enqkjadskbnz']);
}

$to_name    = $config['forwarding_name'];
$to_email   = $config['forwarding_email'];
$from_email = $_POST['emaksjkbkjad'];
$from_name  = $_POST['namsndkjjkba'];
$enquiry    = $_POST['enqkjadskbnz'];

// Set up email headers in the form of:
// Sender: contact@example.com
// From: John Doe via vector.im <john@doe.com>
// Reply-To: John Doe <john@doe.com>
// To: CoolCorp Contact <contact@example.com>
$headers = "Sender: " . $to_email . "\r\n" .
           "From: " . $from_name . " via vector.im " . "<" . $from_email . ">\r\n" .
           "Reply-To: " . $from_name . " <" . $from_email . ">\r\n" .
           "To: " . $to_name . " <" . $to_email . ">";

// Subject is in the form of:
// Webform: Andrew M (andrewm@matrix.org)
$subject = 'Webform: ' . $from_name . ' (' . $from_email . ')';

// Send the email
$success = mail($to_email, $subject, $enquiry, $headers);
if (!$success) {
    $error_message = error_get_last()['message'];
    echo $error_message;
    die();
}

// Redirect user to success page
if (isset($config['success_redirect'])) {
    $new_url = $config['success_redirect'];
    header('Location: ' . $new_url);
}
?>
