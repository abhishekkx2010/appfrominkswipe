<?php
defined('BASEPATH') OR exit('No direct script access allowed');

function registerUser($requestData)
{
	try
	{
		$CI =& get_instance();
		$returnData['upload_message'] = array();
		$sendFileName = '';
		$profileImageName = '';

		if(isset($requestData['name']) && isset($requestData['email']) && isset($requestData['password']) && isset($requestData['mobile']) && isset($requestData['device_id']) && isset($requestData['device_token'])) {

			$condition = array('email' => $requestData['email']);
			$emailExistResp = $CI->api_model->selectAllData('app_users', $condition);

			if(!empty($emailExistResp))
			{
				$returnData['status'] = 1;
				$returnData['data'] = array();
				$returnData['message'] = 'The email id is already registered.';
				return $returnData;
			}

			$encryptPassword = $CI->api_model->encrypt_password($requestData['password']);

		if(isset($requestData['profile_image']) && isset($requestData['image_extension']))
		{
			$profileByte['profile_image'] = $requestData['profile_image'];
			$uploadProfile = uploadImage($profileByte,$requestData['image_extension']);
			$returnData['profile_upload_message'] = $uploadProfile['upload_message'];
		}

		if(isset($uploadProfile['file_name']))
		{
			$profileImageName = $uploadProfile['file_name'];
		}
			
			$gender = isset($requestData['gender']) ? $requestData['gender'] : "";
			$address = isset($requestData['address']) ? $requestData['address'] : "";
			$state = isset($requestData['state']) ? $requestData['state'] : "";
			$city = isset($requestData['city']) ? $requestData['city'] : "";
			$landmark = isset($requestData['landmark']) ? $requestData['landmark'] : "";
			$pincode = isset($requestData['pincode']) ? $requestData['pincode'] : "";
			$deviceType = isset($requestData['device_type']) ? $requestData['device_type'] : "";

			$saveData = array(
				'name' 		    => $requestData['name'],
				'email'         => $requestData['email'],
				'password' 		=> $encryptPassword,	
				'gender'        => $gender,
				'address'		=> $address,
				'state'			=> $state,
				'city'			=> $city,
				'landmark'		=> $landmark,
				'pincode'		=> $pincode,
				'mobile'        => $requestData['mobile'],
				'profile_image' => $profileImageName,
				'device_id' 	=> $requestData['device_id'],
				'device_type' 	=> $deviceType,
				'device_token' 	=> $requestData['device_token'],
				'status' 		=> '1',
				'date_created'	=> date("Y-m-d H:i:s")
			);
			
			$saveResp = $CI->api_model->create('app_users', $saveData);
			
			if(!empty($saveResp))
			{
				$toEmail = $requestData['email'];
				$subject  = "Welcome to Social Society!";
				$messageBody = 'Dear ' .$requestData['name'].',<br/><br/>
				<p>Congratulations, you Are registered successfully.</p> <br/><br/>
				<p>After Registration you may login by using the username and password You entered during registration.</p>
				<b><i>With Regards,</i></b><br/>
				Team'. SITE_NAME;

				$sendmail = send_mail($toEmail,$subject,$messageBody);

				$responseArray['image_complete_url'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$profileImageName;
				$responseArray['user_id'] = $saveResp;
				$responseArray['user_name'] = $requestData['name'];
				$responseArray['email_id'] 	= $requestData['email'];
				$returnData['status'] = 0;
				$returnData['data'] = $responseArray;
				$returnData['message'] = 'Registered Successfully.';
				return $returnData;
			}
			else
			{
				$returnData['status'] = 1;
				$returnData['data'] = [];
				$returnData['message'] = 'Unable to register user, Please try again.';
				return $returnData;
			}
		}
		else
		{	
			$returnData['status'] = 1;
			$returnData['data'] = [];
			$returnData['message'] = 'Invalid or missing required parameters.';
			return $returnData;
		}
	}
	catch(Exception $e)
	{
		$returnData['status'] = 1;
		$returnData['error_code'] = $CI->error_code;
		$returnData['message'] = $e->getMessage();
		return $returnData;
	}
}

function loginUser($requestData)
{
	$CI =& get_instance();

	if(isset($requestData['email']) && isset($requestData['password']) && isset($requestData['device_type']) && isset($requestData['device_id']) && isset($requestData['device_token']))
	 {
	 	$encryptPassword = $CI->api_model->encrypt_password($requestData['password']);

	 	$condition 	= array('email' => $requestData['email'], 'status' => 1);
    	$loginData = $CI->api_model->selectAllData('app_users', $condition);

    	if(!empty($loginData))
    	{
    		if($loginData[0]['password'] == $encryptPassword)
    		{
				$condition = array('id' => $loginData[0]['id']);
				
				$updateData = array('device_id' => $requestData['device_id'], 
									'device_type' => $requestData['device_type'], 
									'device_token' => $requestData['device_token']);

				$updateResponse = $CI->api_model->update('app_users', $condition, $updateData);

				$responseArray['user_id'] = $loginData[0]['id'];
				$responseArray['user_name']	= $loginData[0]['name'];
				$responseArray['email_id'] = $loginData[0]['email'];

				$returnData['status'] = 0;
				$returnData['message'] 	= 'You are successfully logged in.';
				$returnData['data']	= $responseArray;
				return $returnData;
			}
    		else
    		{
				$returnData['status'] = 1;
				$returnData['data'] = array();
				$returnData['message'] = 'Invalid login creadentials.';
				return $returnData;
    		}
    	}
    	else
    	{
			$returnData['status'] = 1;
			$returnData['data'] = array();
			$returnData['message'] = 'The email id is not registered.';
			return $returnData;
    	}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function editUser($requestData)
{
	$CI =& get_instance();
	
	if(isset($requestData['name']) && isset($requestData['user_id']) && isset($requestData['email']) && isset($requestData['device_id']) && isset($requestData['device_type']) &&isset($requestData['device_token']) && isset($requestData['mobile']))
	{
		$condition 	= array('id' => $requestData['user_id']);
    	$getUserData = $CI->api_model->selectAllData('app_users', $condition);

			if(!empty($requestData['profile_image']) && isset($requestData['profile_extension']))
			{
				$profileByte['profile_image'] = $requestData['profile_image'];
				$uploadProfile = uploadImage($profileByte,$requestData['profile_extension']);
				if($uploadProfile['status'] == 0)
				{
					$returnData['profile_image_access_url'] = $uploadProfile['file_url'];
					$returnData['profile_upload_message'] = $uploadProfile['upload_message'];
				}
				else
				{
					$returnData['profile_upload_message'] = $uploadProfile['upload_message'];
				}
			}

			if(!empty($requestData['cover_image']) && isset($requestData['cover_extension']))
			{
				$coverByte['cover_image'] = $requestData['cover_image'];
				$uploadCover = uploadImage($coverByte,$requestData['cover_extension']);
				if($uploadCover['status'] == 0)
				{
					$returnData['cover_image_access_url'] = $uploadCover['file_url'];
					$returnData['cover_upload_message'] = $uploadCover['upload_message'];
				}
				else
				{
					$returnData['cover_upload_message'] = $uploadCover['upload_message'];
				}
			}

			if(isset($uploadProfile['file_name']))
			{
				$profileImageName = $uploadProfile['file_name']; 
			}
			else
			{
				$profileImageName = isset($getUserData[0]['profile_image']) ? $getUserData[0]['profile_image'] : "";
			}
			
			if(isset($uploadCover['file_name']))
			{
				$coverImageName = $uploadCover['file_name']; 
			}
			else
			{
				$coverImageName = isset($getUserData[0]['cover_image']) ? $getUserData[0]['cover_image'] : "";
			}

			$gender = isset($requestData['gender']) ? $requestData['gender'] : "";
			$address = isset($requestData['address']) ? $requestData['address'] : "";
			$state = isset($requestData['state']) ? $requestData['state'] : "";
			$city = isset($requestData['city']) ? $requestData['city'] : "";
			$landmark = isset($requestData['landmark']) ? $requestData['landmark'] : "";
			$pincode = isset($requestData['pincode']) ? $requestData['pincode'] : "";
			$mobile = isset($requestData['mobile']) ? $requestData['mobile'] : "";

			$updateData = array(
				'name' 		    => $requestData['name'],
				'gender'        => $gender,
				'address'		=> $address,
				'state'			=> $state,
				'city'			=> $city,
				'landmark'		=> $landmark,
				'pincode'		=> $pincode,
				'mobile'        => $mobile,
				'profile_image' => $profileImageName,
				'cover_image'	=> $coverImageName,
				'device_id' 	=> $requestData['device_id'],
				'device_type' 	=> $requestData['device_type'],
				'device_token' 	=> $requestData['device_token'],
				'date_modified' => date("Y-m-d H:i:s")
			);

			$updateCondition = array('id' => $requestData['user_id']);
    		$updateResponse = $CI->api_model->update('app_users', $updateCondition, $updateData);

    		$updateData['profile_image_url'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$profileImageName;
    		$updateData['cover_image_url'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$coverImageName;

    		if($updateResponse == 1)
    		{
    			$returnData['status'] = 0;
				$returnData['data'] = $updateData;
				$returnData['message'] = 'The profile is updated successfully.';
				return $returnData;
    		}
    		else
    		{
    			$returnData['status'] = 1;
				$returnData['data'] = array();
				$returnData['message'] = 'Unable to update profile, please try again.';
				return $returnData;
    		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	} 
}

function uploadImage($byteArray,$extension)
{
	$CI =& get_instance();
	if (array_key_exists('profile_image', $byteArray)) 
	{
		$requestData['byte_array'] = $byteArray['profile_image'];
    	$directoryPathArray = array('images', 'profile_images');
	}
	else if(array_key_exists('cover_image', $byteArray))
	{
		$requestData['byte_array'] = $byteArray['cover_image'];
		$directoryPathArray = array('images', 'cover_images');
	}
	else if(array_key_exists('society_image', $byteArray))
	{
		$requestData['byte_array'] = $byteArray['society_image'];
		$directoryPathArray = array('images', 'society_images');
	}
	else if(array_key_exists('announcement_image', $byteArray))
	{
		$requestData['byte_array'] = $byteArray['announcement_image'];
		$directoryPathArray = array('images', 'announcement_images');
	}
	else if(array_key_exists('property_image', $byteArray))
	{
		$requestData['byte_array'] = $byteArray['property_image'];
		$directoryPathArray = array('images', 'property_images');
	}
	else if(array_key_exists('post_image', $byteArray))
	{
		$requestData['byte_array'] = $byteArray['post_image'];
		$directoryPathArray = array('images', 'post_images');
	}
	else if(array_key_exists('poll_image', $byteArray))
	{
		$requestData['byte_array'] = $byteArray['poll_image'];
		$directoryPathArray = array('images', 'poll_images');
	}
	else if(array_key_exists('group_image', $byteArray))
	{
		$requestData['byte_array'] = $byteArray['group_image'];
		$directoryPathArray = array('images', 'group_images');
	}
	else if(array_key_exists('event_image', $byteArray))
	{
		$requestData['byte_array'] = $byteArray['event_image'];
		$directoryPathArray = array('images', 'event_images');
	}

	$directoryPath = $CI->config->item('image_upload_path');

	foreach ($directoryPathArray as $key => $value) 
	{
		$directoryPath = $directoryPath . $value . '/';
		if (!is_dir($directoryPath)) 
		{
			mkdir($directoryPath,0777);
		}
	}
	$date = date("Y-m-d H:i:s");
	$name =  md5($date . mt_rand(1, 99999));
	$filename = $name.'.'.$extension;
	
	$imageData = base64_decode($requestData['byte_array']);
	$sendFileName = '';

	if(file_put_contents($directoryPath . $filename, $imageData))
	{
		$directoryPath = $CI->config->item('uploaded_image_url');

		foreach ($directoryPathArray as $key => $value) 
		{
			$directoryPath = $directoryPath . $value . '/';
		}

		$sendFileName = $directoryPath . $filename;
		$returnData['status'] = 0;
		$returnData['upload_message'] = 'Image upload Successful.';
		$returnData['file_url'] = $sendFileName;
		$returnData['file_name'] = $filename;
		return $returnData;
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['upload_message'] = 'Image upload failed.';
		return $returnData;
	}
}

function forgotPasswordUser($requestData)
{
	$CI =& get_instance();

	if(isset($requestData['email']))
	 {
        $condition 	= array('email' => $requestData['email']);
    	$loginData = $CI->api_model->selectAllData('app_users', $condition);

    	if(isset($loginData) && !empty($loginData))
    	{
    		$otp = verifyContact('email',$requestData['email']);
    		if($otp)
    		{
				$returnData['status']  = 0;
				$returnData['data'] = array();
				$returnData['message'] = 'OTP has been sent to your registered email id.';
				return $returnData;
			}
			else
			{
				$returnData['status']  = 1;
				$returnData['data']    =  array();
				$returnData['message'] = 'Unable to send email.';
				return $returnData;
			}
		}
        else
        {
		      $returnData['status'] = 1;
			  $returnData['data'] = array();
			  $returnData['message'] = 'Email ID is not present in our database.';
			  return $returnData;
        }
    }
	else
	{
		$returnData['status'] = 1;
		$returnData['data'] = [];
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}


function resetPasswordUser($requestData)
{
	$CI =& get_instance();

	if(isset($requestData['email'])  && isset($requestData['otp']) && isset($requestData['password']))
	{
		$emailId = $requestData['email'];
		$otp = $requestData['otp'];

		$otpData = verifyOtp('email', $emailId, $otp);

		if($otpData){
			$condition 	= array('email' => $requestData['email']);
			$encryptPassword = $CI->api_model->encrypt_password($requestData['password']);

			$updateData = array(
				'password' 		=> $encryptPassword,
				'date_modified'	=> date("Y-m-d H:i:s")
				);

			$updateResp = $CI->api_model->update('app_users', $condition, $updateData);

			if(isset($updateResp) && !empty($updateResp))
			{
				$returnData['status'] = 0;
				$returnData['data'] = $updateResp;
				$returnData['message'] = 'Password is reset successfully.';
				return $returnData;
			}
			else
			{
				$returnData['status'] = 1;
				$returnData['data'] = array();
				$returnData['message'] = 'Unable to reset password.';
				return $returnData;
			}
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['data'] = array();
			$returnData['message'] = $otpData;
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function getUserData($requestData)
{
	$CI =& get_instance();
	$propertyCount = 0;

	if(isset($requestData['user_id']))
	{
		$condition = array('id' => $requestData['user_id'], 'status' => 1);
    	$userData = $CI->api_model->selectAllData('app_users', $condition);

    	$propertyCondition = array('user_id' => $requestData['user_id'], 'status'=> 1);
    	$propertyData = $CI->api_model->selectAllData('user_properties',$propertyCondition);
    	if(!empty($propertyData))
    	{
    		$propertyCount = count($propertyData);
    	}
    	if(!empty($userData))
    	{
    		$userData[0]['property_count'] = $propertyCount;
    		$userData[0]['profile_image_url'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$userData[0]['profile_image'];
    		$userData[0]['cover_image_url'] = $CI->config->item('uploaded_image_url').'images/cover_images/'.$userData[0]['cover_image'];
			$returnData['status'] = 0;
			$returnData['data'] = $userData;
			$returnData['message'] = 'view profile successfully.';
			return $returnData;
    	}
    	else
    	{
    		$returnData['status'] = 1;
			$returnData['data'] = array();
			$returnData['message'] = 'User is not found in database.';
			return $returnData;
    	}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function logoutUser($requestData)
{
	$CI =& get_instance();

	if(isset($requestData['user_id']))
	 {

	 	$userCondition 	= array('id' => $requestData['user_id']);
		$updateUserData = array(
			'device_token' 		=> '',
			'date_modified'	=> date("Y-m-d H:i:s")
			);
		$updateResp = $CI->api_model->update('app_users', $userCondition, $updateUserData);

    	if(isset($updateResp) && !empty($updateResp))
    	{
				$returnData['status'] = 0;
				$returnData['message'] 	= 'You are successfully logged out.';
				$returnData['data']	= array();
				return $returnData;
		}
    	else
    	{
			$returnData['status'] = 1;
			$returnData['data'] = array();
			$returnData['message'] = 'User is not registered or unable to logged out';
			return $returnData;
    	}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

