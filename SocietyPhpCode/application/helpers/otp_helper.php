<?php
defined('BASEPATH') OR exit('No direct script access allowed');

function verifyContact($type, $detail)
{
    $CI =& get_instance();
    $condition = array('type' => $type, 'contact_detail' => $detail, 'status' => 0);
    $checkContact = $CI->api_model->selectAllData('verify_otp', $condition);

    if(empty($checkContact))
    {
        $otp = rand(100000, 999999);
        $saveData['type'] = $type;
        $saveData['contact_detail'] = $detail;
        $saveData['otp'] = $otp;
        $saveData['status'] = 0;
        $saveData['date_created'] = date("Y-m-d H:i:s");
        $inserted = $CI->api_model->create('verify_otp', $saveData);
        return sendOtp($type,$detail,$otp);
    }
    else 
    {
        foreach ($checkContact as $key => $value) 
        {
            $condition = array('id' => $value['id']);
            $updateData = array('status' => -1);
            $updateResponse = $CI->api_model->update('verify_otp', $condition, $updateData);
        }

        $otp = rand(100000, 999999);
        $saveData['type'] = $type;
        $saveData['contact_detail'] = $detail;
        $saveData['otp'] = $otp;
        $saveData['status'] = 0;
        $saveData['date_created'] = date("Y-m-d H:i:s");
        $inserted = $CI->api_model->create('verify_otp', $saveData);
        return sendOtp($type,$detail,$otp);
    }
}

function verifyForgotContact($type, $detail)
{
    $CI =& get_instance();
    $condition = array('type' => $type, 'contact_detail' => $detail, 'status' => 0);
    $checkContact = $CI->api_model->selectAllData('verify_otp', $condition);

    if(empty($checkContact))
    {
        $otp = rand(100000, 999999);
        $saveData['type'] = $type;
        $saveData['contact_detail'] = $detail;
        $saveData['otp'] = $otp;
        $saveData['status'] = 0;
        $saveData['date_created'] = date("Y-m-d H:i:s");
        $inserted = $CI->api_model->create('verify_otp', $saveData);
        return sendForgotOtp($type,$detail,$otp);
    }
    else 
    {
        foreach ($checkContact as $key => $value) 
        {
            $condition = array('id' => $value['id']);
            $updateData = array('status' => -1);
            $updateResponse = $CI->api_model->update('verify_otp', $condition, $updateData);
        }

        $otp = rand(100000, 999999);
        $saveData['type'] = $type;
        $saveData['contact_detail'] = $detail;
        $saveData['otp'] = $otp;
        $saveData['status'] = 0;
        $saveData['date_created'] = date("Y-m-d H:i:s");
        $inserted = $CI->api_model->create('verify_otp', $saveData);
        return sendOtp($type,$detail,$otp);
    }
}

function verifyOtp($type,$detail,$otp)
{
    $CI =& get_instance();
    $condition = array('contact_detail' => $detail, 'status' => 0, 'otp' => $otp);
    $verifyOtp = $CI->api_model->selectAllData('verify_otp', $condition);
   
    if(!empty($verifyOtp) )
    {
        $updateData    = array('status' => 1);
        $CI->api_model->update('verify_otp', $condition, $updateData);
        return true;
    }
    else
    {
        return false;
    }
}

function sendForgotOtp($type, $contectDetail, $otp)
{
    $CI =& get_instance();

    if($type == 'email')
    {
        $subject = 'Email verification | ' . SITE_NAME;
        $message = '<p>Dear User,</p>
                    <p>To help you in resetting the password, we have generated one time password for you as: ' . $otp . '.</p>
                    <p>Please enter this into the application as new password now.</p>
                    <p>This is one time password hence please change your password also, as prompted.</p>
                    <p><b><i>With Regards,</i></b><br/>Team ' . SITE_NAME . '</p>';

        return send_mail($contectDetail, $subject, $message);
    }
    else
    {
        $message = 'Your Social Society Mobile number verification code is '. $otp;
        return send_sms($contectDetail, $message);
    }
}

function sendOtp($type, $contectDetail, $otp)
{
    $CI =& get_instance();

    if($type == 'email')
    {
        $subject = 'Email verification | ' . SITE_NAME;
        $message = '<p>Dear User,</p>
                    <p>Your ' . SITE_NAME . ' email verification code is: ' . $otp . '</p>
                    <p>Please enter this into the application when Prompted.</p>
                    <p>This one-time email verification code is time sensitive and valid for a single use only.</p>
                    <p><b><i>With Regards,</i></b><br/>Team ' . SITE_NAME . '</p>';

        return send_mail($contectDetail, $subject, $message);
    }
    else
    {
        $message = 'Your Social Society Mobile number verification code is '. $otp;
        return send_sms($contectDetail, $message);
    }
}

function testOtp($type,$detail)
{
    $CI =& get_instance();
    $condition = array('type' => $type,'contact_detail' => $detail,'status'=>0);
    $testOtp = $CI->api_model->selectAllData('verify_otp', $condition);
    return $testOtp[0]['otp'];
}





