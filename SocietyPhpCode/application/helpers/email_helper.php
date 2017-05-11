<?php
function send_mail($toEmail, $subject, $msg, $attachment = null)
{
	$CI =& get_instance();
	$CI->config->load('email');
	$CI->load->library('PHPMailer');
	$CI->load->library('SMTP');

	$fromEmail = $CI->config->item('from_email');
	$fromName = $CI->config->item('from_name');
	$subject = $CI->config->item('subject_prefix') . $subject;
	$smtpHost = $CI->config->item('smtp_host');
	$smtpPort = $CI->config->item('smtp_port');
	$smtpUser = $CI->config->item('smtp_user');
	$smtpPass = $CI->config->item('smtp_pass');

	$mail = new PHPMailer();
	$mail->isSMTP();
	$mail->Host = $smtpHost;
	$mail->Port = $smtpPort;
	$mail->SMTPAuth = true;
    $mail->Username = $smtpUser;
    $mail->Password = $smtpPass;
    $mail->setFrom($fromEmail, $fromName);
    $mail->addReplyTo($fromEmail, $fromName);
	$mail->addAddress($toEmail);
    $mail->Subject = $subject;
    $mail->msgHTML($msg);
    $mail->AltBody = 'If you can\'t view the email, contact us';

    if($attachment != null){
    	$mail->addAttachment($attachment);
    }

    $mail->SMTPOptions = array(
    	'ssl' => array(
    		'verify_peer' => false,
    		'verify_peer_name' => false,
    		'allow_self_signed' => true
    		)
    	);

    if (!$mail->send()) {
    	return false;
    } else {
		return true;
	}
}
?>