<?php
defined('BASEPATH') OR exit('No direct script access allowed');

function registerSociety($requestData)
{
	$CI =& get_instance();
	$createdBy = '';
	$societyImageName = '';

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['name']) 
		&& isset($requestData['address']) 
		&& isset($requestData['user_id']) 
		&& isset($requestData['state']) 
		&& isset($requestData['city']) 
		&& isset($requestData['pincode']))
	{
		$condition = array('id' => $requestData['user_id']);
		$userData = $CI->api_model->selectAllData('app_users', $condition);

		$createdBy = isset($userData[0]['name']) ? $userData[0]['name'] : '';
		$userEmail = isset($userData[0]['email']) ? $userData[0]['email'] : '';

		if(isset($requestData['society_image']) && isset($requestData['image_extension']))
		{
			$societyByte['society_image'] = $requestData['society_image'];
			$uploadSociety = uploadImage($societyByte,$requestData['image_extension']);
			$returnData['society_upload_message'] = $uploadSociety['upload_message'];
		}

		if(isset($uploadSociety['file_name']))
		{
			$societyImageName = $uploadSociety['file_name'];
		}

		$landmark = isset($requestData['landmark']) ? $requestData['landmark'] : "";
		$area = isset($requestData['area']) ? $requestData['area'] : "";
		$post = isset($requestData['post']) ? $requestData['post'] : "No";

		$insertPost = 0;
		if($post == 'Yes')
		{
			$insertPost = 1;
		}

		$landmark = isset($requestData['landmark']) ? $requestData['landmark'] : "";
		$pincode = $requestData['pincode'];
		$post_name = isset($requestData['post_name']) ? $requestData['post_name'] : "";

		$saveData = array(
			'name' 		    => $requestData['name'],
			'address'		=> $requestData['address'],
			'state'			=> $requestData['state'],
			'city'			=> $requestData['city'],
			'landmark'		=> $landmark,
			'area'			=> $area,
			'pincode'		=> $requestData['pincode'],
			'society_image' => $societyImageName,
			'user_id'		=> $requestData['user_id'],
			'post'			=> $insertPost,
			'post_name' 	=> $post_name,
			'created_by'    => $createdBy,
			'status' 		=> '1',
			'date_created'	=> date("Y-m-d H:i:s")
			);

		$saveSociety = $CI->api_model->create('society_request', $saveData);

		if(!empty($saveSociety))
		{
			$toEmail = $userEmail;
			$subject  = "Society registration request received! | " . SITE_NAME;
			$messageBody = 'Dear ' . $createdBy . ',<br/><br/>
			Thank you for contacting us.<br/><br/>
			Your request to add a new housing society is successfully registered.<br/><br/>
			We will review your details and send you a notification informing you the status on the suggested society.<br/><br/>
			<b><i>With Regards,</i></b><br/></br>
			Team ' . SITE_NAME;

			$sendmail = send_mail($toEmail,$subject,$messageBody);

			$sendNotification = pushNotification($requestData['user_id']);

			$responseArray['society_id'] = $saveSociety;
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $responseArray;
			$returnData['message'] = 'Society registration request received.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Unable to register request, please try again.';
			return $returnData;
		}
	}
	else
	{	
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function searchSociety($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['society_name']) && isset($requestData['pincode']) && isset($requestData['user_id']))
	{
		$condition = array('name LIKE ' => '%' . $requestData['society_name'] . '%', 'pincode' => $requestData['pincode'], 'status' => 1);

		$societyData = $CI->api_model->selectAllData('society',$condition);

		$returnData = array();
		if(!empty($societyData))
		{
			foreach ($societyData as $key => $currentSociety) {
				$societyData[$key]['society_image_url'] = '';
				if(!empty($societyData[$key]['society_image']))
				{
					$societyData[$key]['society_image_url'] = $CI->config->item('uploaded_image_url').'images/society_images/'.$societyData[$key]['society_image'];
				}
			}
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $societyData;
			$returnData['message'] = 'Success.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Society is not registered.';
			return $returnData;	
		}
	}
	else
	{	
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function userComplaint($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);
	
	if(isset($requestData['society_id'])
		&& isset($requestData['subject'])  
		&& isset($requestData['description'])
		&& isset($requestData['user_id']))
	{
		$saveData = array(
			'society_id' => $requestData['society_id'], 
			'subject' => $requestData['subject'],
			'description' => $requestData['description'],
			'status' => '1',
			'created_by'	=> $requestData['user_id']
			);

		$saveComplaint = $CI->api_model->create('complaints', $saveData);

		if(!empty($saveComplaint))
		{	
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Complaint is registered successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Complaint is not registered.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function userAnnouncement($requestData)
{	
	$CI =& get_instance();
	$announcementImageName = '';

	$userStatus = userCurrentStatus($requestData['user_id']);

	if (isset($requestData['society_id']) 
		&& isset($requestData['announcement_title'])
		&& isset($requestData['description'])
		&& isset($requestData['user_id'])) 
	{   
		if (isset($requestData['announcement_image']) && isset($requestData['image_extension'])) 
		{
			$announcementByte['announcement_image'] = $requestData['announcement_image'];
			$uploadAnnouncement = uploadImage($announcementByte,$requestData['image_extension']);
			$returnData['announcement_upload_message'] = $uploadAnnouncement['upload_message'];
		}

		if (isset($uploadAnnouncement['file_name'])) 
		{
			$announcementImageName = $uploadAnnouncement['file_name'];
		}

		$saveData = array(
			'society_id' => $requestData['society_id'],
			'announcement_title' => $requestData['announcement_title'],
			'description' => $requestData['description'],
			'announcement_image' => $announcementImageName,
			'status' => '1',
			'created_by' => $requestData['user_id'],
			'created_on' => date("Y-m-d H:i:s"),
			);
		$saveAnnouncement = $CI->api_model->create('user_announcements', $saveData);

		if(!empty($saveAnnouncement))
		{	
			
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus; 
			$returnData['data'] = array();
			$returnData['message'] = 'Announcement is created successfully.';

			$userCondition = array('id'=> $requestData['user_id']);
			$userData = $CI->api_model->selectAllData('app_users',$userCondition);

			$societyCondition = 'society_id = '.$requestData['society_id'] . ' AND status = 1 AND user_id NOT IN ('.$requestData['user_id'].')';
			$societyData = $CI->api_model->selectDistinctUser($societyCondition);
			foreach ($societyData as $ukey => $uvalue) 
			{	
				$msg = $userData[0]['name'].' | Posted a new announcement.';

				$societyUserCondition = array('id' => $societyData[$ukey]['user_id']);
				$societyUserData = 	$CI->api_model->selectAllData('app_users',$societyUserCondition);
				$regid = isset($societyUserData[0]['device_token']) ? $societyUserData[0]['device_token'] : "";	
				$api_access_key = $CI->config->item('api_access_key');
				$message = array('message' => $msg,'event_type' =>'4','event_id' => $saveAnnouncement,'society_id' => $requestData['society_id']);

				$pushNotification = $CI->api_model->push_notification($regid,$api_access_key,$message);
				$createNotification = addNotification($uvalue['user_id'], $requestData['society_id'], $msg,$saveAnnouncement,4);	
			}
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Announcement is not registered.';
			return $returnData;	
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] ='Invalid or missing required parameters.';
		return $returnData;	
	}
}

function getPropertyType()
{
	$CI =& get_instance();
	$CI->load->library('TypeMaster');

	$condition = array('status' => 1);
	$property = $CI->api_model->selectAllData('property_type_master',$condition);

	foreach ($property as $key => $value) {
		$propertyMapData[] = (new TypeMaster())->mapCkisColumns($value);
	}

	return $propertyMapData;
}

function registerUserProperty($requestData)
{
	$CI =& get_instance();
	$propertyImageName = '';

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['user_id']) && isset($requestData['society_id']) && isset($requestData['property_name']))
	{
		if(isset($requestData['property_image']) && isset($requestData['image_extension']))
		{
			$propertyByte['property_image'] = $requestData['property_image'];
			$uploadProperty = uploadImage($propertyByte,$requestData['image_extension']);
			$returnData['property_upload_message'] = $uploadProperty['upload_message'];
		}

		if(isset($uploadProperty['file_name']))
		{
			$propertyImageName = $uploadProperty['file_name'];
		}

		$propertyType = isset($requestData['property_type']) ? $requestData['property_type'] : '';
		$userType = isset($requestData['user_type']) ? $requestData['user_type'] : '';
		$houseNo = isset($requestData['house_no']) ? $requestData['house_no'] : '';
		$isAvailable = isset($requestData['is_available']) ? $requestData['is_available'] : '';
		$availableFrom = isset($requestData['available_from']) ? $requestData['available_from'] : '';

		$saveData = array(
			'property_name' => $requestData['property_name'],
			'society_id' => $requestData['society_id'],
			'user_id' => $requestData['user_id'],
			'property_type' => $propertyType,
			'user_type' => $userType,
			'house_no' => $houseNo,
			'is_available_for_rent' => $isAvailable,
			'rent_availability_date' => $availableFrom,
			'property_image' => $propertyImageName,
			'status' => 1,
			'created_on' => date("Y-m-d H:i:s")
			);

		$saveProperty = $CI->api_model->create('user_properties',$saveData);

		if(!empty($saveProperty))
		{

			$userCondition = array('user_id'=> $requestData['user_id'], 'society_id'=> $requestData['society_id']);
			$userData = $CI->api_model->getSocietyUserDetails($userCondition);

			$societyCondition = 'society_id = '.$requestData['society_id'] . ' AND property_name = "'.$requestData['property_name'].'" AND house_no = "'.$houseNo.'" AND status = 1 AND user_id NOT IN ('.$requestData['user_id'].')';
			$societyData = $CI->api_model->selectDistinctUser($societyCondition);

			foreach ($societyData as $ukey => $uvalue) 
			{

				$societyUserCondition = array('id' => $uvalue['user_id']);
				$societyUserData = 	$CI->api_model->selectAllData('app_users',$societyUserCondition);

				$user_id_array = array();
				foreach ($societyUserData as $key1 => $row) {
					//Notification to user who is having same property
					if(!in_array($row['id'], $user_id_array))
					{
						$regid = isset($row['device_token']) ? $row['device_token'] : "";
						$api_access_key = $CI->config->item('api_access_key');
						$msg = $userData[0]['user_name'].' | Added a property same as yours having details as '.$propertyType.'-'.$requestData['property_name'].'/'.$houseNo.', '.$userType.', '.$userData[0]['society_name'].', '.$userData[0]['pincode'].'';
						$message = array('message' => $msg, 'event_type' =>'5', 'event_id' =>$saveProperty, 'society_id' => $requestData['society_id']); 
						$pushNotification = $CI->api_model->push_notification($regid,$api_access_key,$message);
						$createNotification = addNotification($uvalue['user_id'], $requestData['society_id'], $msg, $saveProperty, 5);

						array_push($user_id_array, $row['id']);
					}
				}
			}

			//Notification to user who is adding property which already belongs to other user
			if(!empty($societyData))
			{
				$creator_regid = isset($userData[0]['device_token']) ? $userData[0]['device_token'] : "";
				$api_access_key = $CI->config->item('api_access_key');
				$msg = $userData[0]['user_name'].' | You have added a same property which already belongs to other user having details as '.$propertyType.'-'.$requestData['property_name'].'/'.$houseNo.', '.$userType.', '.$userData[0]['society_name'].', '.$userData[0]['pincode'].'';
				$message = array('message' => $msg, 'event_type' =>'5', 'event_id' =>$saveProperty, 'society_id' => $requestData['society_id']); 
				$pushNotification = $CI->api_model->push_notification($creator_regid,$api_access_key,$message);
				$createNotification = addNotification($requestData['user_id'], $requestData['society_id'], $msg, $saveProperty, 5);
			}

			$responseArray['property_id'] = $saveProperty;
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $responseArray;
			$returnData['message'] = 'Property is Registered Successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Unable to register property, please try again.';
			return $returnData;
		}
	}
	else
	{	
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function deleteUserProperty($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['property_id']) && isset($requestData['user_id']))
	{
		$condition = array('id' => $requestData['property_id'], 'user_id' => $requestData['user_id']);
		$updateData = array('status'=> -1);
		$deleteProperty = $CI->api_model->update('user_properties', $condition, $updateData);

		if(!empty($deleteProperty))
		{	
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['message'] = 'Property is deleted Successful.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['message'] = 'Unable to delete property, please try again.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}

}

function getUserProperty($requestData)
{
	$CI =& get_instance();
	$condition = array();

	$userStatus = userCurrentStatus($requestData['user_id']);
	if(isset($requestData['user_id']))
	{
		if(isset($requestData['society_id']))
		{
			$condition = array('user_id' => $requestData['user_id'], 'society_id' => $requestData['society_id'], 'status' => 1);
		}
		else
		{
			$condition = array('user_id' => $requestData['user_id'], 'status' => 1);
		}

		$getProperty = $CI->api_model->selectAllData('user_properties',$condition);

		if(!empty($getProperty))
		{
			foreach ($getProperty as $key => $value) 
			{
				$getProperty[$key]['property_image_url'] = '';
				$societyCondition = array('id' => $getProperty[$key]['society_id']);
				$societyData = $CI->api_model->selectAllData('society',$societyCondition);

				if(!empty($societyData))
				{
					$getProperty[$key]['society_name'] = $societyData[0]['name'];	
					$getProperty[$key]['society_city'] = $societyData[0]['city'];	
				}
				if(!empty($getProperty[$key]['property_image']))
				{
					$getProperty[$key]['property_image_url'] = $CI->config->item('uploaded_image_url').'images/property_images/'.$getProperty[$key]['property_image'];
				}

			}
			
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $getProperty;
			$returnData['message'] = 'Success.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'No Property is registered.';
			return $returnData;
		}

	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}


function userGetAnnouncements($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['user_id']))
	{
		$condition = array('created_by' =>$requestData['user_id'],'status' => 1);
		$announcementData = $CI->api_model->selectAllData('user_announcements',$condition);
		$returnData = array();

		if(!empty($announcementData))
		{
			foreach ($announcementData as $key => $currentAnnouncements) 
			{
				$nowDate = date("Y-m-d H:i:s");
				$announceDate = $announcementData[$key]['created_on'];
				$announcementData[$key]['days_pass'] = time_elapsed_string($announceDate);

				$announcementData[$key]['announcement_image_url'] = '';
				if(!empty($announcementData[$key]['announcement_image']))
				{
					$announcementData[$key]['announcement_image_url'] = $CI->config->item('uploaded_image_url').'images/announcement_images/'.$announcementData[$key]['announcement_image'];
				}
			}

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $announcementData;
			$returnData['message'] = 'Get announcement';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $announcementData;
			$returnData['message'] = 'Announcement is not registered.';
			return $returnData;
		}
	}
	else
	{	
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function editUserProperty($requestData)
{
	$CI =& get_instance();
	$propertyImageName = '';

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['property_id']) && isset($requestData['property_name']) && isset($requestData['user_id']))
	{
		$condition = array('id' => $requestData['property_id']);
		$propertyData = $CI->api_model->selectAllData('user_properties',$condition);

		if(!empty($requestData['property_image']) && isset($requestData['image_extension']))
		{
			$propertyByte['property_image'] = $requestData['property_image'];
			$uploadProperty = uploadImage($propertyByte,$requestData['image_extension']);
			$returnData['property_upload_message'] = $uploadProperty['upload_message'];
		}

		$propertyImageName = isset($uploadProperty['file_name']) ? $uploadProperty['file_name'] : $propertyData[0]['property_image'];

		$propertyName = isset($requestData['property_name']) ? $requestData['property_name'] : '';
		$propertyType = isset($requestData['property_type']) ? $requestData['property_type'] : '';
		$userType = isset($requestData['user_type']) ? $requestData['user_type'] : '';
		$houseNo = isset($requestData['house_no']) ? $requestData['house_no'] : '';
		$isAvailable = isset($requestData['is_available']) ? $requestData['is_available'] : '';
		$availableFrom = isset($requestData['available_from']) ? $requestData['available_from'] : '';

		$updateData = array(
			'property_name' => $propertyName,
			'property_type' => $propertyType,
			'user_type' => $userType,
			'house_no' => $houseNo,
			'is_available_for_rent' => $isAvailable,
			'rent_availability_date' => $availableFrom,
			'property_image' => $propertyImageName,
			'updated_on' => date("Y-m-d H:i:s")
			);
		$updateCondition = array('id' => $requestData['property_id']);
		$updatePropertyData = $CI->api_model->update('user_properties',$updateCondition,$updateData);

		if($updatePropertyData == 1)
		{	
			// $userStatus = 1;
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Property is updated successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Unable to update Property, please try again.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function societyAnnouncements($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if (isset($requestData['society_id']) && isset($requestData['user_id']))
	{
		$condition = array('society_id' => $requestData['society_id'], 'status' => 1);
		$societyAnnouncementData = $CI->api_model->selectAllData('user_announcements',$condition);
		$returnData = array();

		if (!empty($societyAnnouncementData))
		{
			foreach ($societyAnnouncementData as $key => $currentAnnouncements) 
			{
				$userCondition = array('id' =>$societyAnnouncementData[$key]['created_by']);
				$userData = $CI->api_model->selectAllData('app_users',$userCondition);
				if(!empty($userData))
				{
					$societyAnnouncementData[$key]['user_name'] = $userData[0]['name'];
				}
				$nowDate = date("Y-m-d H:i:s");
				$announceDate = $societyAnnouncementData[$key]['created_on'];
				$societyAnnouncementData[$key]['days_pass'] = time_elapsed_string($announceDate);
				if(!empty($societyAnnouncementData[$key]['announcement_image']))
				{
					$societyAnnouncementData[$key]['announcement_images_url'] = $CI->config->item('uploaded_image_url').'images/announcement_images/'.$societyAnnouncementData[$key]['announcement_image'];	
				}
				else
				{
					$societyAnnouncementData[$key]['announcement_images_url'] = '';
				}
				
			}
			
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $societyAnnouncementData;
			$returnData['message'] = 'Get announcements.';
			return $returnData;
		}

		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $societyAnnouncementData;
			$returnData['message'] = 'Currently no announcements for this society.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}

}

function getUserSociety($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['user_id']))
	{
		$condition = array('user_id' => $requestData['user_id'], 'status'=>1);
		$distinctSociety = $CI->api_model->selectDistinctSociety($condition);
		if(!empty($distinctSociety))
		{
			foreach ($distinctSociety as $key => $value) 
			{
				$condition = array('id' => $distinctSociety[$key]['society_id']);
				$result = $CI->api_model->selectAllData('society',$condition);
				if(!empty($result))
				{
					$societyData[] = $result[0];
				}
			}	
		}

		if (!empty($societyData))
		{
			foreach ($societyData as $skey => $currentsocietyData) 
			{
				$societyData[$skey]['society_images_url'] = '';
				if(!empty($societyData[$key]['society_image']))
				{	
					$societyData[$skey]['society_images_url'] = $CI->config->item('uploaded_image_url').'images/society_images/'.$societyData[$key]['society_image'];
				}
			}
			
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $societyData;
			$returnData['message'] = 'View society successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $societyData;
			$returnData['message'] = 'Society is not created.';
			return $returnData;
		}
		
		return $societyData; 
	}
}

function getMySocietyMember($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if (isset($requestData['society_id'])) 
	{
		$condition = array('society_id' => $requestData['society_id'], 'status' => 1);
		$userData = $CI->api_model->getSocietyMember($condition);

		if(!empty($userData))
		{
			foreach ($userData as $skey => $currentuserData) 
			{	
				$userData[$skey]['profile_image_url'] = '';

				if(!empty($userData[$skey]['profile_image']))
				{
					$userData[$skey]['profile_image_url'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$userData[$skey]['profile_image'];
				}
			}

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $userData;
			$returnData['message'] = 'View society members.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Society members are not registered.';
			return $returnData;
		}
	} 
}

function addUserPost($requestData)
{
	$CI =& get_instance();
	$postImageName = '';

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['society_id']) && isset($requestData['user_id']) && isset($requestData['post_description']))
	{
		if(isset($requestData['post_image']) && isset($requestData['image_extension']))
		{
			$postByte['post_image'] = $requestData['post_image'];
			$uploadPost = uploadImage($postByte,$requestData['image_extension']);
			$returnData['post_upload_message'] = $uploadPost['upload_message'];
		}

		if(isset($uploadPost['file_name']))
		{
			$postImageName = $uploadPost['file_name'];
		}

		$saveData = array(
			'user_id' => $requestData['user_id'],
			'society_id' => $requestData['society_id'],
			'post_description' => $requestData['post_description'],
			'post_image' => $postImageName,
			'status' => 1,
			'created_on' => date("Y-m-d H:i:s")
			);
		$savePost = $CI->api_model->create('user_posts',$saveData);

		if(!empty($savePost))
		{	
			
			$responseArray['post_id'] = $savePost;
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $responseArray;
			$returnData['message'] = 'Post is created Successfully.';

			$userCondition = array('id'=> $requestData['user_id']);
			$userData = $CI->api_model->selectAllData('app_users',$userCondition);
			$societyCondition = 'society_id = '.$requestData['society_id'] . ' AND status = 1 AND user_id NOT IN ('.$requestData['user_id'].')';
			$societyData = $CI->api_model->selectDistinctUser($societyCondition);
			foreach ($societyData as $ukey => $uvalue) 
			{
				$societyUserCondition = array('id' => $societyData[$ukey]['user_id']);
				$societyUserData = 	$CI->api_model->selectAllData('app_users',$societyUserCondition);
				$regid = isset($societyUserData[0]['device_token']) ? $societyUserData[0]['device_token'] : "";

				$api_access_key = $CI->config->item('api_access_key');

				$msg = $userData[0]['name'].' | Added a new post.';

				$message = array('message' => $msg,'event_type' =>'1','event_id' =>$savePost,'society_id' => $requestData['society_id']);

				$pushNotification = $CI->api_model->push_notification($regid,$api_access_key,$message);
				$createNotification = addNotification($uvalue['user_id'],$requestData['society_id'], $msg,$savePost,1);
			}
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Unable to create post, please try again.';
			return $returnData;
		}
	}
	else
	{	
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function getPostBySociety($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['society_id']) && isset($requestData['user_id']))
	{
		$condition = array('society_id' => $requestData['society_id'], 'status' => 1 );
		$postData = $CI->api_model->selectAllData('user_posts', $condition);

		if(!empty($postData))
		{
			foreach ($postData as $key => $value) 
			{
				$nowDate = date("Y-m-d H:i:s");
				$postDate = $postData[$key]['created_on'];
				$postData[$key]['days_pass'] = time_elapsed_string($postDate);
				if(!empty($postData[$key]['post_image']))
				{
					$postData[$key]['post_image_url'] = $CI->config->item('uploaded_image_url').'images/post_images/'.$postData[$key]['post_image'];	
				}
				else
				{
					$postData[$key]['post_image_url'] = '';
				}

				$commentCondition = array('uc.post_id' => $postData[$key]['id'], 'uc.status' => 1);
				$commentData = $CI->api_model->getComments($commentCondition);
				if(!empty($commentData))
				{
					foreach ($commentData as $ckey => $cvalue)
					{
						$comment[$ckey]['id'] = $ckey;
						$comment[$ckey]['comment'] = $commentData[$ckey]['comment'];
						$comment[$ckey]['name'] = $commentData[$ckey]['name'];
						$comment[$ckey]['profile_image'] = $CI->config->item('uploaded_image_url').'images/profile_images/' . $commentData[$ckey]['profile_image'];
						$comment[$ckey]['user_id'] = $commentData[$ckey]['id'];
					}
					$postData[$key]['comments'] =$comment;	
				}
				else
				{
					$postData[$key]['comments'] = array();
				}
				
				$userCondition = array('id' => $postData[$key]['user_id']);
				$userData = $CI->api_model->selectAllData('app_users',$userCondition);

				if(!empty($userData))
				{
					$postData[$key]['created_by'] = $userData[0]['name'];
					$postData[$key]['profile_image_url'] = '';
					if(!empty($userData[0]['profile_image']))
					{
						$postData[$key]['profile_image_url'] = $CI->config->item('uploaded_image_url').'images/profile_images/' . $userData[0]['profile_image'];	
					}
				}
				unset($comment);
			}			
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $postData;
			$returnData['message'] = 'View posts successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Post is not created.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function getPostByUser($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['user_id']))
	{
		$condition = array('user_id' => $requestData['user_id'], 'status' => 1 );
		$postData = $CI->api_model->selectAllData('user_posts', $condition);

		$userCondition = array('id' => $requestData['user_id']);
		$userData = $CI->api_model->selectAllData('app_users',$userCondition);

		if(!empty($postData))
		{
			foreach ($postData as $key => $value) 
			{
				$postDate = $postData[$key]['created_on'];
				$postData[$key]['days_pass'] = time_elapsed_string($postDate);
				$postData[$key]['post_image_url'] = '';
				if(!empty($postData[$key]['post_image']))
				{
					$postData[$key]['post_image_url'] = $CI->config->item('uploaded_image_url').'images/post_images/'.$postData[$key]['post_image'];
				}
				
				if(!empty($userData))
				{
					$postData[$key]['created_by'] = $userData[0]['name'];
					$postData[$key]['profile_image_url'] ='';
					if(!empty($userData[0]['profile_image']))
					{
						$postData[$key]['profile_image_url'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$userData[0]['profile_image'];
					}
				}
			}
			
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $postData;
			$returnData['message'] = 'View posts successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Post is not created.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function addPostComment($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['user_id']) && isset($requestData['post_id']) && isset($requestData['comment']))
	{
		$saveData = array(
			'user_id'  		=> $requestData['user_id'],
			'post_id' 		=> $requestData['post_id'],
			'comment' 		=> $requestData['comment'],
			'status'  		=> 1,
			'created_on'	=> date("Y-m-d H:i:s")
			);

		$saveComment = $CI->api_model->create('user_post_comments',$saveData);

		if(!empty($saveComment))
		{	
			
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'A comment is added Successfully.';
			return $returnData;
		}
		else
		{
			$returnData['account_status'] = 1;
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Unable to add comment, please try again.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function addUserPoll($requestData)
{
	$CI =& get_instance();
	$pollImageName = '';
	$returnData['share_message'] = '';

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['society_id']) && isset($requestData['user_id']) && isset($requestData['poll_question']) && isset($requestData['shared_with']) && isset($requestData['poll_end_date']) && isset($requestData['poll_option_1']) && isset($requestData['poll_option_2']))
	{
		if(isset($requestData['poll_image']) && isset($requestData['image_extension']))
		{
			$pollByte['poll_image'] = $requestData['poll_image'];
			$uploadPoll = uploadImage($pollByte,$requestData['image_extension']);
			$returnData['poll_upload_message'] = $uploadPoll['upload_message'];
		}

		if(isset($uploadPoll['file_name']))
		{
			$pollImageName = $uploadPoll['file_name'];
		}

		$pollTitle = isset($requestData['poll_title']) ? $requestData['poll_title'] : '';
		$pollOption3 = isset($requestData['poll_option_3']) ? $requestData['poll_option_3'] : '';
		$pollOption4 = isset($requestData['poll_option_4']) ? $requestData['poll_option_4'] : '';
		$pollOption5 = isset($requestData['poll_option_5']) ? $requestData['poll_option_5'] : '';
		$pollOption6 = isset($requestData['poll_option_6']) ? $requestData['poll_option_6'] : '';

		$saveData = array(
			'society_id' => $requestData['society_id'],
			'poll_title' => $requestData['poll_title'],
			'poll_question' => $requestData['poll_question'],
			'poll_option_1' => $requestData['poll_option_1'],
			'poll_option_2' => $requestData['poll_option_2'],
			'poll_image' => $pollImageName,
			'poll_option_3' => $pollOption3,
			'poll_option_4' => $pollOption4,
			'poll_option_5' => $pollOption5,
			'poll_option_6' => $pollOption6,
			'poll_end_date' => $requestData['poll_end_date'],
			'shared_with'   => $requestData['shared_with'],
			'created_by' => $requestData['user_id'],
			'created_on' => date("Y-m-d H:i:s"),
			'status' => 1
			);		 

		$savePolls = $CI->api_model->create('user_polls',$saveData);

		if(!empty($savePolls))
		{
			$userCondition = array('id'=> $requestData['user_id']);
			$userData = $CI->api_model->selectAllData('app_users',$userCondition);
			if(isset($requestData['private_users']))
			{
				$user_id_array = array();
				foreach ($requestData['private_users'] as $key => $value) {
					$shareData = array('user_id' => $value, 'society_id' => $requestData['society_id'], 'poll_id' => $savePolls);
					$sharePoll = $CI->api_model->create('poll_share',$shareData);

					if(!empty($sharePoll))
					{
						if(!empty($userData))
						{
							$msg = $userData[0]['name'].' | Shared a new poll.';
						}

						if(!in_array($value, $user_id_array))
						{
							$societyUserCondition = array('id' => $value);
							$societyUserData = 	$CI->api_model->selectAllData('app_users',$societyUserCondition);
							$regid = isset($societyUserData[0]['device_token']) ? $societyUserData[0]['device_token'] : "";	
							$api_access_key = $CI->config->item('api_access_key');

							$message = array('message' => $msg,'event_type' => '2','event_id' => $savePolls,'society_id'=> $requestData['society_id']);
							$pushNotification = $CI->api_model->push_notification($regid,$api_access_key,$message);

							$createNotification = addNotification($value,$requestData['society_id'],$msg,$savePolls,2);

							array_push($user_id_array, $value);
						}
					}
				}
				
			}
			else if(isset($requestData['private_group']))
			{
				foreach ($requestData['private_group'] as $key => $value) 
				{
					$condition = array('group_id' => $value);
					$groupMembers = $CI->api_model->selectAllData('society_group_members', $condition);

					if(!empty($groupMembers))
						foreach ($groupMembers as $gkey => $gvalue) 
						{
							$userId[] = $gvalue['app_user_id']; 
						}
					}

					if(!empty($userId))
					{
						$userIds = array_unique($userId);
						foreach ($userIds as $ukey => $uvalue) 
						{
							$shareData = array('user_id' => $uvalue, 'society_id' => $requestData['society_id'], 'poll_id' => $savePolls);
							$sharePoll = $CI->api_model->create('poll_share',$shareData);

							if(!empty($sharePoll))
							{
								if(!empty($userData))
								{
									$msg = $userData[0]['name'].' | Shared a new poll.';
								}

								$societyUserCondition = array('id' => $uvalue);
								$societyUserData = 	$CI->api_model->selectAllData('app_users',$societyUserCondition);
								$regid = isset($societyUserData[0]['device_token']) ? $societyUserData[0]['device_token'] : "";	
								$api_access_key = $CI->config->item('api_access_key');

								$message = array('message' => $msg,'event_type' => '2','event_id' => $savePolls,'society_id' =>$requestData['society_id']);
								$pushNotification = $CI->api_model->push_notification($regid,$api_access_key,$message);
								$createNotification = addNotification($uvalue,$requestData['society_id'],$msg,$savePolls,2);
							}	
						}
					}
					else
					{
						$returnData['share_message'] = 'Unable to share poll.';
					}
				}
				else if($requestData['shared_with'] == 'public')
				{
					$msg = $userData[0]['name'].' | Added a new poll.';
					$societyCondition = 'society_id = '.$requestData['society_id'] . ' AND status = 1 AND user_id NOT IN ('.$requestData['user_id'].')';
					$societyData = $CI->api_model->selectDistinctUser($societyCondition);
					foreach ($societyData as $ukey => $uvalue) 
					{
						$societyUserCondition = array('id' => $uvalue['user_id']);
						$societyUserData = 	$CI->api_model->selectAllData('app_users',$societyUserCondition);
						$regid = isset($societyUserData[0]['device_token']) ? $societyUserData[0]['device_token'] : "";	
						$api_access_key = $CI->config->item('api_access_key');

						$message = array('message' => $msg,'event_type' => '2','event_id' => $savePolls,'society_id' => $requestData['society_id']);
						$pushNotification = $CI->api_model->push_notification($regid,$api_access_key,$message);

						$createNotification = addNotification($uvalue['user_id'],$requestData['society_id'],$msg,$savePolls,2);	
					}
				}

				$responseArray['poll_id'] = $savePolls;
				$returnData['status'] = 0;
				$returnData['account_status'] = $userStatus;
				$returnData['data'] = $responseArray;
				$returnData['message'] = 'Poll is created Successfully.';
				return $returnData;
			}
			else
			{
				$returnData['status'] = 1;
				$returnData['account_status'] = $userStatus;
				$returnData['data'] = array();
				$returnData['message'] = 'Unable to create poll, please try again.';
				return $returnData;
			}
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Invalid or missing required parameters.';
			return $returnData;
		}
}

function getUserPoll($requestData)
{
	$CI =& get_instance();
	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['society_id']) && isset($requestData['user_id']))
	{
		$condition = array('user_id' => $requestData['user_id'], 'society_id' => $requestData['society_id']);
		$chkExist = $CI->api_model->selectAllData('poll_share', $condition);


		if(!empty($chkExist))
		{
			foreach ($chkExist as $key => $value) {
				$pollId[] = $value['poll_id'];
			}
			$pollIds = implode(',', $pollId);
			
			$PollCondition = '(id IN('.$pollIds.') AND `status` = 1 AND poll_end_date > Now()) OR (`society_id` = '.$requestData['society_id'].' AND `shared_with` = '."'public'".') AND poll_end_date > Now() AND status = 1 OR (`shared_with` = '."'private'".' AND created_by ='.$requestData['user_id'].' AND status = 1 AND society_id = '.$requestData['society_id'].' AND poll_end_date > Now())'; 
			$pollData = $CI->api_model->selectAllData('user_polls',$PollCondition);
		}
		else
		{
			$PollCondition = '`society_id` = '.$requestData['society_id'].' AND `shared_with` = '."'public'".' AND poll_end_date > Now() AND status = 1 OR (`shared_with` = '."'private'".' AND created_by ='.$requestData['user_id'].' AND status = 1 AND society_id = '.$requestData['society_id'].' AND poll_end_date > Now())';
			$pollData = $CI->api_model->selectAllData('user_polls',$PollCondition);
		}

		$shareCondition = array('society_id' => $requestData['society_id'], 'share_result' => 1, 'status' => 0);
		$sharePollData = $CI->api_model->selectAllData('user_polls',$shareCondition);

		$pollData = array_merge($pollData, $sharePollData);

		function record_sort($records, $field, $reverse=false)
		{
		    $hash = array();
		   
		    foreach($records as $record)
		    {
		        $hash[$record[$field]] = $record;
		    }
		   
		    ($reverse)? krsort($hash) : ksort($hash);
		   
		    $records = array();
		   
		    foreach($hash as $record)
		    {
		        $records []= $record;
		    }
		   
		    return $records;
		}

		$pollData = record_sort($pollData, "id");
		
		if(!empty($pollData))
		{
			foreach ($pollData as $pkey => $pvalue) {

				$pollData[$pkey]['poll_image_url'] = '';
				if(!empty($pollData[$pkey]['poll_image']))
				{
					$pollData[$pkey]['poll_image_url'] = $CI->config->item('uploaded_image_url').'images/poll_images/'.$pollData[$pkey]['poll_image'];
				}

				$userCondition = array('id' => $pollData[$pkey]['created_by']);
				$userData = $CI->api_model->selectAllData('app_users',$userCondition);

				if(!empty($userData))
				{
					$pollData[$pkey]['user_profile_image'] = '';
					$pollData[$pkey]['user_name'] = $userData[0]['name'];
					if(!empty($userData[0]['profile_image']))
					{
						$pollData[$pkey]['user_profile_image'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$userData[0]['profile_image'];
					}
				}
			}

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $pollData;
			$returnData['message'] = 'Polls are fetched Successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Polls are not found create polls.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function publishPollResult($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['society_id']) && isset($requestData['poll_id']))
	{
		$updateData = array('share_result' => 1);
		$updateCondition = array('id' => $requestData['poll_id'], 'society_id' => $requestData['society_id']);
		$updatePollData = $CI->api_model->update('user_polls',$updateCondition,$updateData);

		if(!empty($updatePollData))
		{	
			
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Poll result successfully published.';
			return $returnData;
		}
		else
		{
			$returnData['account_status'] = 1;
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Unable to publish poll result, please try again.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function getArchivePoll($requestData)
{	
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);
	if(isset($requestData['society_id']) && isset($requestData['user_id']))
	{
		
		$PollCondition = '((`status`= 0 OR `poll_end_date` < "'.date("Y-m-d H:i:s").'") AND created_by = '.$requestData['user_id'].' AND society_id = '.$requestData['society_id'].')';
		$pollData = $CI->api_model->selectAllData('user_polls',$PollCondition);


		if(!empty($pollData))
		{
			foreach ($pollData as $pkey => $pvalue) {
				$pollData[$pkey]['poll_image_url'] = '';
				if(!empty($pollData[$pkey]['poll_image']))
				{
					$pollData[$pkey]['poll_image_url'] = $CI->config->item('uploaded_image_url').'images/poll_images/'.$pollData[$pkey]['poll_image'];
				}
				$userCondition = array('id' => $pollData[$pkey]['created_by']);
				$userData = $CI->api_model->selectAllData('app_users',$userCondition);
				$pollData[$pkey]['user_name'] = $userData[0]['name'];
				$pollData[$pkey]['user_profile_image'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$userData[0]['profile_image'];
			}

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $pollData;
			$returnData['message'] = 'Polls are fetched Successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Polls are not found create poll.';
			return $returnData;
		}

	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function editEndDate($requestData)
{
	$CI =& get_instance();
	$now = new DateTime();
	$now  = $now->format('Y-m-d H:i:s'); 

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['poll_id']) && isset($requestData['new_end_date']) && isset($requestData['user_id']) && $requestData['new_end_date'] > $now)
	{
		$condition = 'poll_end_date > NOW() AND id ='.$requestData['poll_id'];
		$endDate = $CI->api_model->selectAllData('user_polls', $condition);

		if(!empty($endDate))
		{
			$updateData = array('poll_end_date' => $requestData['new_end_date']);
			$updateCondition = array('id' => $requestData['poll_id']);
			$updatePollData = $CI->api_model->update('user_polls',$updateCondition,$updateData);
			if(!empty($updatePollData))
			{	

				$returnData['status'] = 0;
				$returnData['account_status'] = $userStatus;
				$returnData['data'] = array();
				$returnData['message'] = 'Poll end date is Postponed.';
				return $returnData;
			}
			else
			{
				$returnData['status'] = 0;
				$returnData['account_status'] = $userStatus;
				$returnData['data'] = array();
				$returnData['message'] = 'Unable to update the poll date, please try again.';
				return $returnData;
			}

		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Poll date is finished.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function archivePoll($requestData)
{
	$CI =& get_instance();
	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['poll_id']) && $requestData['user_id'])
	{
		$updateCondition = array('id' => $requestData['poll_id'], 'created_by' => $requestData['user_id']);
		$updateData = array('status' => 0);
		$updatePollData = $CI->api_model->update('user_polls',$updateCondition,$updateData);
		if($updatePollData == true)
		{	

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Poll is archived successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Poll is already archived, or no polls found.';
			return $returnData;
		}

	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function deletePoll($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['poll_id']) && $requestData['user_id'])
	{
		$updateCondition = array('id' => $requestData['poll_id'], 'created_by' => $requestData['user_id']);
		$updateData = array('status' => -1);
		$updatePollData = $CI->api_model->update('user_polls',$updateCondition,$updateData);
		if($updatePollData == true)
		{	

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Poll is deleted successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Poll is already deleted or no poll found.';
			return $returnData;
		}

	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function time_elapsed_string($datetime, $full = false) 
{
	$now = new DateTime;
	$ago = new DateTime($datetime);
	$diff = $now->diff($ago);

	$diff->w = floor($diff->d / 7);
	$diff->d -= $diff->w * 7;

	$string = array(
		'y' => 'year',
		'm' => 'month',
		'w' => 'week',
		'd' => 'day',
		'h' => 'hour',
		'i' => 'minute',
		's' => 'second',
		);
	foreach ($string as $k => &$v) {
		if ($diff->$k) {
			$v = $diff->$k . ' ' . $v . ($diff->$k > 1 ? 's' : '');
		} else {
			unset($string[$k]);
		}
	}

	if (!$full) $string = array_slice($string, 0, 1);
	return $string ? implode(', ', $string) . ' ago' : 'just now';

}

function addPollOption($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['poll_id']) && isset($requestData['user_id']) && isset($requestData['selected_option']))
	{
		$condition = array('poll_id' => $requestData['poll_id'], 'user_id' => $requestData['user_id']);
		$chkOptionExist = $CI->api_model->selectAllData('poll_submissions', $condition);

		$pollcondition = array('id' => $requestData['poll_id']);
		$polldata = $CI->api_model->selectAllData('user_polls', $pollcondition);

		if($polldata[0]['status'] == 1)
		{
			if(empty($chkOptionExist))
			{
				$saveOption = array('poll_id' => $requestData['poll_id'], 'user_id' => $requestData['user_id'], 'selected_option'=> $requestData['selected_option']);
				$addOption = $CI->api_model->create('poll_submissions', $saveOption);
				if(!empty($addOption))
				{

					$returnData['status'] = 0;
					$returnData['pollstatus'] = 1;
					$returnData['account_status'] = $userStatus;
					$returnData['data'] = array();
					$returnData['message'] = 'Poll option has added successfully.';
					return $returnData;
				}
				else
				{
					$returnData['status'] = 1;
					$returnData['pollstatus'] = 0;
					$returnData['account_status'] = $userStatus;
					$returnData['data'] = array();
					$returnData['message'] = 'Unable to add poll option, please try again.';
					return $returnData;
				}
			}
			else
			{
				$returnData['status'] = 1;
				$returnData['pollstatus'] = 0;
				$returnData['account_status'] = $userStatus;
				$returnData['data'] = array();
				$returnData['message'] = 'You have already selected some option.';
				return $returnData;
			}
		}
		else
		{
			$returnData['status'] = 0;
			$returnData['pollstatus'] = -1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Poll no longer exist.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['pollstatus'] = 0;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function getPollDetails($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['poll_id']) && isset($requestData['user_id']))
	{
		$condition = array('id' => $requestData['poll_id']);
		$pollData = $CI->api_model->selectAllData('user_polls', $condition);

		$pollCondition = array('poll_id' => $requestData['poll_id']);
		$submitPollData = $CI->api_model->selectAllData('poll_submissions', $pollCondition);

		$pollstatus = $pollData[0]['status'];
		$pollshareresult = $pollData[0]['share_result'];

		if($pollstatus == '1')
		{
			if($pollshareresult == '0')
			{
				$pollcheck = 1; //Poll is open
			}
		}
		else if($pollstatus == '0')
		{
			if($pollshareresult == '0')
			{
				$pollcheck = 0; //Poll is closed
			}
			else
			{
				$pollcheck = 2; //Poll result shared
			}
		}
		else
		{
			if($pollstatus == '-1')
			{
				$pollcheck = 0; //Poll is closed
			}
		}

		$pollData[0]['pollcheck'] 	  = $pollcheck;
		$pollData[0]['option1_count'] = 0;
		$pollData[0]['option2_count'] = 0;
		$pollData[0]['option3_count'] = 0;
		$pollData[0]['option4_count'] = 0;
		$pollData[0]['option5_count'] = 0;
		$pollData[0]['option6_count'] = 0;
		$pollData[0]['selected_option'] = '';
		$pollData[0]['end_date'] = '';
		$pollData[0]['archive_poll'] = '';
		$pollData[0]['delete_poll'] = '';
		if(!empty($submitPollData))
		{
			foreach ($submitPollData as $key => $value) 
			{
				if($pollData[0]['poll_option_1'] == $submitPollData[$key]['selected_option'] && !empty($pollData[0]['poll_option_1']))
				{
					$pollOption1[] = $submitPollData[$key]['selected_option']; 
				}
				else if($pollData[0]['poll_option_2'] == $submitPollData[$key]['selected_option'] && !empty($pollData[0]['poll_option_2']))
				{
					$pollOption2[] = $submitPollData[$key]['selected_option']; 
				}
				else if($pollData[0]['poll_option_3'] == $submitPollData[$key]['selected_option'] && !empty($pollData[0]['poll_option_3']))
				{
					$pollOption3[] = $submitPollData[$key]['selected_option']; 
				}
				else if($pollData[0]['poll_option_4'] == $submitPollData[$key]['selected_option'] && !empty($pollData[0]['poll_option_4']))
				{
					$pollOption4[] = $submitPollData[$key]['selected_option']; 
				}
				else if($pollData[0]['poll_option_5'] == $submitPollData[$key]['selected_option'] && !empty($pollData[0]['poll_option_5']))
				{
					$pollOption5[] = $submitPollData[$key]['selected_option']; 
				}
				else if($pollData[0]['poll_option_6'] == $submitPollData[$key]['selected_option'] && !empty($pollData[0]['poll_option_6']))
				{
					$pollOption6[] = $submitPollData[$key]['selected_option']; 
				}

				if($requestData['user_id'] == $submitPollData[$key]['user_id'])
				{
					$pollData[0]['selected_option'] = $submitPollData[$key]['selected_option'];
				}
			}


			if(!empty($pollOption1))
			{
				$pollData[0]['option1_count'] = count($pollOption1);
			}
			if(!empty($pollOption2))
			{
				$pollData[0]['option2_count'] = count($pollOption2);
			}
			if(!empty($pollOption3))
			{
				$pollData[0]['option3_count'] = count($pollOption3);
			}
			if(!empty($pollOption4))
			{
				$pollData[0]['option4_count'] = count($pollOption4);
			}
			if(!empty($pollOption5))
			{
				$pollData[0]['option5_count'] = count($pollOption5);
			}
			if(!empty($pollOption6))
			{
				$pollData[0]['option6_count'] = count($pollOption6);
			}
		}

		$userCondition = array('id' => $pollData[0]['created_by']);
		$userData = $CI->api_model->selectAllData('app_users',$userCondition);

		if(!empty($userData))
		{
			$pollData[0]['user_profile_image'] = '';
			$pollData[0]['user_name'] = $userData[0]['name'];
			if(!empty($userData[0]['profile_image']))
			{
				$pollData[0]['user_profile_image'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$userData[0]['profile_image'];
			}
		}

		if($pollData[0]['created_by'] == $requestData['user_id'])
		{
			$pollData[0]['end_date'] = 'enable';
			$pollData[0]['archive_poll'] = 'enable';
			$pollData[0]['delete_poll'] = 'enable';
		}

		if(!empty($pollData))
		{	
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $pollData;
			$returnData['message'] = 'Poll details are fetched successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Poll data has not found.';
			return $returnData;
		}

	}
}

function getOffers($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['society_id']) && isset($requestData['user_id']))
	{
		$condition = array('society_id' => $requestData['society_id']);
		$getOffers = $CI->api_model->selectAllData('offer_mapping',$condition);
		if (!empty($getOffers)) 
		{	
			foreach ($getOffers as $key => $value) 
			{
				$offerId[] = $getOffers[$key]['offer_id'];
			}
			$offerIds = implode(',',$offerId);

			$offerCondition = '(id IN('.$offerIds.') AND status = 1) OR shared_with = '."'Public'".' AND status = 1 AND expire_on > now()';
			$offerData = $CI->api_model->selectAllData('offers_master', $offerCondition);			
		}
		else
		{
			$offerCondition = 'shared_with = '."'Public'".' AND status = 1 AND expire_on > now()';
			$offerData = $CI->api_model->selectAllData('offers_master', $offerCondition);
		}

		if(!empty($offerData))
		{
			foreach ($offerData as $okey => $ovalue) 
			{
				$offerData[$okey]['offer_image_url'] = '';
				$offerData[$okey]['offer_logo_url'] = '';
				if(!empty($offerData[$okey]['offer_image']))
				{
					$offerData[$okey]['offer_image_url'] = $CI->config->item('uploaded_image_url').'images/offer_images/'.$offerData[$okey]['offer_image'];
				}
				if(!empty($offerData[$okey]['offer_logo']))
				{
					$offerData[$okey]['offer_logo_url'] = $CI->config->item('uploaded_image_url').'images/offer_images/'.$offerData[$okey]['offer_logo'];
				}
				$offerData[$okey]['days_pass'] = time_elapsed_string($offerData[$okey]['created_on']);
			}
			$sendNotification = pushNotification($requestData['user_id']);
			$returnData['status'] = 0;	
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $offerData;
			$returnData['message'] = 'Offers are fetched successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;	
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $offerData;
			$returnData['message'] = 'No offers are found.';
			return $returnData;
		}

	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}


function addSocietyUserGroups($requestData)
{
	$CI =& get_instance();
	$groupImageName = '';

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['society_id']) && isset($requestData['user_id']) && isset($requestData['group_name']) && isset($requestData['poll_id']) && isset($requestData['poll_option']))
	{
		if(isset($requestData['group_image']) && isset($requestData['image_extension']))
		{
			$groupByte['group_image'] = $requestData['group_image'];
			$uploadGroup = uploadImage($groupByte,$requestData['image_extension']);
			$returnData['group_upload_message'] = $uploadGroup['upload_message'];
		}

		if(isset($uploadGroup['file_name']))
		{
			$groupImageName = $uploadGroup['file_name'];
		}

		$saveGroupData = array(
			'society_id' => $requestData['society_id'],
			'group_image' => $groupImageName,
			'group_name' => $requestData['group_name'],
			'created_by' => $requestData['user_id'],
			'status' => 1
			);

		$createGroup = $CI->api_model->create('society_user_groups', $saveGroupData);

		if(!empty($createGroup))
		{
			$condition = array('poll_id' => $requestData['poll_id'], 'selected_option' => $requestData['poll_option']);
			$submitedPollData =	 $CI->api_model->selectAllData('poll_submissions',$condition);

			if(!empty($submitedPollData))
			{
				foreach ($submitedPollData as $key => $value)
				{
					$saveMember = array('group_id' => $createGroup, 'app_user_id' => $value['user_id']);
					$addMember = $CI->api_model->create('society_group_members', $saveMember);	
				}
			}

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'The group is created successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Unable to create group, please try again.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function getSocietyUserGroups($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['society_id']) &&  $requestData['user_id'])
	{
		$condition = array('app_user_id' => $requestData['user_id']);
		$groupData = $CI->api_model->selectAllData('society_group_members', $condition);
		if(!empty($groupData))
		{
			foreach ($groupData as $key => $value) 
			{
				$groupId[] = $value['group_id'];	
			}

			$groupIds = implode(',',$groupId);

			$groupCondition = 'society_id ='.$requestData['society_id'].' AND created_by='.$requestData['user_id'].' AND status = 1';
			$societyGroupData = $CI->api_model->selectAllData('society_user_groups', $groupCondition);

			if(!empty($societyGroupData))
			{
				foreach ($societyGroupData as $gkey => $gvalue)
				{
					$societyGroupData[$gkey]['group_image_url'] = '';
					if(!empty($societyGroupData[$gkey]['group_image']))
					{
						$societyGroupData[$gkey]['group_image_url'] = $CI->config->item('uploaded_image_url').'images/group_images/'.$societyGroupData[$gkey]['group_image'];
					}
					$userCondition = array('id' => $societyGroupData[$gkey]['created_by']);
					$userName = $CI->api_model->selectAllData('app_users', $userCondition);
					$societyGroupData[$gkey]['user_name'] = $userName[0]['name'];
				}
			}

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $societyGroupData;
			$returnData['message'] = 'Group is fetched successfully';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'No group is created.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function getSocietyGroupMember($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['group_id']))
	{
		$condition = array('group_id' => $requestData['group_id']);
		$groupMembers = $CI->api_model->selectAllData('society_group_members', $condition);

		if(!empty($groupMembers))
		{
			foreach ($groupMembers as $key => $value) 
			{
				$groupMembers[$key]['profile_image_url'] ='';
				$userCondition = array('id' => $groupMembers[$key]['app_user_id']);
				$userData = $CI->api_model->selectAllData('app_users', $userCondition);
				$groupMembers[$key]['user_name'] = $userData[0]['name'];
				if(!empty($userData[0]['profile_image']))
				{
					$groupMembers[$key]['profile_image_url'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$userData[0]['profile_image'];
				}
			}

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $groupMembers;
			$returnData['message'] = 'Members are fetched successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'No members are Found.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function deleteSocietyGroup($requestData)
{
	$CI =& get_instance();
	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['group_id']) && isset($requestData['user_id']))
	{
		$condition = array('id' => $requestData['group_id']);
		$updateData = array('status' => -1);
		$deleteGroup = $CI->api_model->update('society_user_groups', $condition, $updateData);

		if(!empty($deleteGroup))
		{		
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'The group is deleted successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Unable to delete group, please try again.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function createSocietyEvent($requestData)
{
	$CI =& get_instance();
	$eventImageName = '';

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['society_id']) && isset($requestData['user_id']) && isset($requestData['event_date']) && isset($requestData['shared_with']) && $requestData['title'])
	{
		if(isset($requestData['event_image']) && isset($requestData['image_extension']))
		{
			$eventByte['event_image'] = $requestData['event_image'];
			$uploadEvent = uploadImage($eventByte,$requestData['image_extension']);
			$returnData['event_upload_message'] = $uploadEvent['upload_message'];
		}

		if(isset($uploadEvent['file_name']))
		{
			$eventImageName = $uploadEvent['file_name'];
		}

		$description = isset($requestData['description']) ? $requestData['description'] : '';
		$address = isset($requestData['address']) ? $requestData['address'] : '';
		$landmark = isset($requestData['landmark']) ? $requestData['landmark'] : '';
		$state = isset($requestData['state']) ? $requestData['state'] : '';
		$city = isset($requestData['city']) ? $requestData['city'] : '';
		$postalCode = isset($requestData['postal_code']) ? $requestData['postal_code'] : '';

		$saveData = array(
			'society_id' => $requestData['society_id'],
			'title' => $requestData['title'],
			'event_date' => $requestData['event_date'],
			'description' => $description,
			'event_image' => $eventImageName,
			'address' => $address,
			'landmark' => $landmark,
			'state' => $state,
			'city' => $city,
			'postal_code' => $postalCode,
			'shared_with' => $requestData['shared_with'],
			'created_by' => $requestData['user_id'],
			'created_on' => date("Y-m-d H:i:s"),
			'status' => 1
			);

		$saveEvent = $CI->api_model->create('society_events',$saveData);

		if(!empty($saveEvent))
		{
			$userCondition = array('id'=> $requestData['user_id']);
			$userData = $CI->api_model->selectAllData('app_users',$userCondition);
			if(isset($requestData['private_users']))
			{	
				foreach ($requestData['private_users'] as $key => $value) {
					$shareData = array('user_id' => $value, 'society_id' => $requestData['society_id'], 'event_id' => $saveEvent);
					$shareEvent = $CI->api_model->create('event_share',$shareData);

					if(!empty($shareEvent))
					{
						if(!empty($userData))
						{
							$msg = $userData[0]['name'].' | Shared a new event.';
						}

						$societyUserCondition = array('id' => $value);
						$societyUserData = 	$CI->api_model->selectAllData('app_users',$societyUserCondition);
						$regid = isset($societyUserData[0]['device_token']) ? $societyUserData[0]['device_token'] : "";	

						$api_access_key = $CI->config->item('api_access_key');

						$message = array('message' => $msg,'event_type' =>'3','event_id' => $saveEvent,'society_id' => $requestData['society_id']); 

						$pushNotification = $CI->api_model->push_notification($regid,$api_access_key,$message);
						$createNotification = addNotification($value,$requestData['society_id'], $msg,$saveEvent,3);
					}
				}
			}
			else if(isset($requestData['private_group']))
			{
				foreach ($requestData['private_group'] as $key => $value) 
				{
					$condition = array('group_id' => $value);
					$groupMembers = $CI->api_model->selectAllData('society_group_members', $condition);
					if(!empty($groupMembers))
						foreach ($groupMembers as $gkey => $gvalue) 
						{
							$userId[] = $gvalue['app_user_id']; 
						}
					}

					if(!empty($userId))
					{
						$userIds = array_unique($userId);
						foreach ($userIds as $ukey => $uvalue) 
						{
							$shareData = array('user_id' => $uvalue, 'society_id' => $requestData['society_id'], 'event_id' => $saveEvent);
							$shareEvent = $CI->api_model->create('event_share',$shareData);
							if(!empty($shareEvent))
							{
								if(!empty($userData))
								{
									$msg = $userData[0]['name'].' | Shared a new event.';
								}
								$societyUserCondition = array('id' => $uvalue);
								$societyUserData = 	$CI->api_model->selectAllData('app_users',$societyUserCondition);
								$regid = isset($societyUserData[0]['device_token']) ? $societyUserData[0]['device_token'] : "";	
								$api_access_key = $CI->config->item('api_access_key');
								$message = array('message' => $msg,'event_type' =>'3','event_id' =>$saveEvent,'society_id' =>$requestData['society_id']);
								$pushNotification = $CI->api_model->push_notification($regid,$api_access_key,$message);
								$createNotification = addNotification($uvalue,$requestData['society_id'], $msg,$saveEvent,3);
							}

						}
					}
				}
				else if($requestData['shared_with'] == 'public')
				{
					$msg = $userData[0]['name'].' | Published a new event.';
					$societyCondition = 'society_id = '.$requestData['society_id'] . ' AND status = 1 AND user_id NOT IN ('.$requestData['user_id'].')';
					$societyData = $CI->api_model->selectDistinctUser($societyCondition);
					foreach ($societyData as $ukey => $uvalue) 
					{

						$societyUserCondition = array('id' => $uvalue['user_id']);
						$societyUserData = 	$CI->api_model->selectAllData('app_users',$societyUserCondition);
						$regid = isset($societyUserData[0]['device_token']) ? $societyUserData[0]['device_token'] : "";	
						$api_access_key = $CI->config->item('api_access_key');

						$message = array('message' => $msg,'event_type' =>'3','event_id' =>$saveEvent,'society_id' =>$requestData['society_id']);
						$pushNotification = $CI->api_model->push_notification($regid,$api_access_key,$message);

						$createNotification = addNotification($uvalue['user_id'],$requestData['society_id'], $msg,$saveEvent,3);	
					}
				}

				$responseArray['event_id'] = $saveEvent;
				$returnData['status'] = 0;
				$returnData['account_status'] = $userStatus;
				$returnData['data'] = $responseArray;
				$returnData['message'] = 'An event is created Successfully.';
				return $returnData;
			}
			else
			{
				$returnData['status'] = 1;
				$returnData['account_status'] = $userStatus;
				$returnData['data'] = array();
				$returnData['message'] = 'Unable to create event, please try again.';
				return $returnData;
			}
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Invalid or missing required parameters.';
			return $returnData;
		}
}

function getSocietyEvent($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['society_id']) && isset($requestData['user_id']))
	{
		$condition = array('user_id' => $requestData['user_id'], 'society_id' => $requestData['society_id']);
		$chkExist = $CI->api_model->selectAllData('event_share', $condition);
		if(!empty($chkExist))
		{
			foreach ($chkExist as $key => $value) {
				$eventId[] = $value['event_id'];
			}
			$eventIds = implode(',', $eventId);
			if(isset($requestData['event_date']))
			{
				$fromDate = date("Y-m-d 00:00:00 ", strtotime($requestData['event_date']));
				$toDate = date("Y-m-d 23:59:59 ", strtotime($requestData['event_date']));

				$eventCondition = '(id IN('.$eventIds.") AND `status` = 1 AND event_date >= '".$fromDate."' AND event_date <= '".$toDate."') OR ( society_id = " . $requestData['society_id'] . " AND event_date >= '".$fromDate."' AND event_date <= '".$toDate."' AND status = 1 and shared_with = 'public') OR (society_id = " . $requestData['society_id'] . " AND event_date >= '".$fromDate."' AND event_date <= '".$toDate."' AND status = 1 and shared_with = 'private' AND created_by = '".$requestData['user_id']."')";	
			}
			else
			{
				$eventCondition = '(id IN('.$eventIds.") AND `status` = 1) OR ( society_id = " . $requestData['society_id'] . " AND status = 1 and shared_with = 'public') OR(society_id = " . $requestData['society_id'] . " AND status = 1 and shared_with = 'private' AND created_by = '".$requestData['user_id']."')";
			}			

			$eventData = $CI->api_model->selectAllData('society_events',$eventCondition);
		}
		else
		{
			if(isset($requestData['event_date']))
			{
				$fromDate = date("Y-m-d 00:00:00 ", strtotime($requestData['event_date']));
				$toDate = date("Y-m-d 23:59:59 ", strtotime($requestData['event_date']));

				$eventCondition = "( society_id = " . $requestData['society_id'] . " AND event_date >= '".$fromDate."' AND event_date <= '".$toDate."' AND status = 1 and shared_with = 'public') OR (society_id = " . $requestData['society_id'] . " AND event_date >= '".$fromDate."' AND event_date <= '".$toDate."' AND status = 1 and shared_with = 'private' AND created_by = '".$requestData['user_id']."')";	
			}
			else
			{
				$eventCondition = "( society_id = " . $requestData['society_id'] . " AND status = 1 and shared_with = 'public') OR(society_id = " . $requestData['society_id'] . " AND status = 1 and shared_with = 'private' AND created_by = '".$requestData['user_id']."')";
			}

			$eventData = $CI->api_model->selectAllData('society_events',$eventCondition);
		}

		if(!empty($eventData))
		{
			foreach ($eventData as $pkey => $pvalue) {
				$eventData[$pkey]['event_image_url'] = '';
				if(!empty($eventData[$pkey]['event_image']))
				{
					$eventData[$pkey]['event_image_url'] = $CI->config->item('uploaded_image_url').'images/event_images/'.$eventData[$pkey]['event_image'];
				}
				$eventData[$pkey]['event_date_time'] = date("jS F, Y h:i A", strtotime($eventData[$pkey]['event_date']));
				$eventData[$pkey]['date'] = date("Y-m-d",strtotime($eventData[$pkey]['event_date']));
				$eventData[$pkey]['month'] = date(" F, Y", strtotime($eventData[$pkey]['event_date']));
				$eventData[$pkey]['time'] = date("h:i A", strtotime($eventData[$pkey]['event_date']));
				$eventData[$pkey]['days_pass'] = time_elapsed_string($eventData[$pkey]['created_on']); 
				$userCondition = array('id' => $eventData[$pkey]['created_by']);
				$userData = $CI->api_model->selectAllData('app_users',$userCondition);
				if(!empty($userData))
				{
					$eventData[$pkey]['user_name'] = $userData[0]['name'];
					$eventData[$pkey]['user_profile_image'] = '';
					if(!empty($userData[0]['profile_image']))
					{
						$eventData[$pkey]['user_profile_image'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$userData[0]['profile_image'];
					}		
				}

			}

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $eventData;
			$returnData['message'] = 'Events are fetched successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Events are not found create an event.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function getArchiveEvent($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['society_id']) && isset($requestData['user_id']))
	{
		$condition = array('user_id' => $requestData['user_id'], 'society_id' => $requestData['society_id']);
		$chkExist = $CI->api_model->selectAllData('event_share', $condition);
		
		$eventCondition = '((`event_date` < "'.date("Y-m-d H:i:s").'") AND created_by = '.$requestData['user_id'].' AND society_id = '.$requestData['society_id'].')';
		$eventData = $CI->api_model->selectAllData('society_events',$eventCondition);
		if(!empty($eventData))
		{
			foreach ($eventData as $pkey => $pvalue) {
				$eventData[$pkey]['event_image_url'] = '';
				$eventData[$pkey]['user_profile_image'] = '';
				if(!empty($eventData[$pkey]['event_image']))
				{
					$eventData[$pkey]['event_image_url'] = $CI->config->item('uploaded_image_url').'images/event_images/'.$eventData[$pkey]['event_image'];
				}
				$eventData[$pkey]['event_date_time'] = date("jS F, Y h:i A", strtotime($eventData[$pkey]['event_date']));
				$eventData[$pkey]['date'] = date("Y-m-d",strtotime($eventData[$pkey]['event_date']));
				$eventData[$pkey]['month'] = date(" F, Y", strtotime($eventData[$pkey]['event_date']));
				$eventData[$pkey]['time'] = date("h:i A", strtotime($eventData[$pkey]['event_date']));
				$userCondition = array('id' => $eventData[$pkey]['created_by']);
				$userData = $CI->api_model->selectAllData('app_users',$userCondition);
				$eventData[$pkey]['user_name'] = $userData[0]['name'];
				if(!empty($userData[0]['profile_image']))
				{
					$eventData[$pkey]['user_profile_image'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$userData[0]['profile_image'];
				}
			}

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $eventData;
			$returnData['message'] = 'Events are fetched Successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Events are Not found.';
			return $returnData;
		}

	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function deleteEvent($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['event_id']) && $requestData['user_id'])
	{
		$updateCondition = array('id' => $requestData['event_id'], 'created_by' => $requestData['user_id']);
		$updateData = array('status' => -1);
		$updateEventData = $CI->api_model->update('society_events',$updateCondition,$updateData);
		if($updateEventData == true)
		{

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Event is deleted successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'An event is already deleted or no event found.';
			return $returnData;
		}

	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function submitEvent($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['event_id']) && isset($requestData['user_id']) && isset($requestData['status']))
	{
		$condition = array('event_id'=> $requestData['event_id'],'user_id' => $requestData['user_id']);
		$statusExist = $CI->api_model->selectAllData('event_user_status',$condition);

		$eventcondition = array('id'=> $requestData['event_id']);
		$eventdata = $CI->api_model->selectAllData('society_events',$eventcondition);

		if($eventdata[0]['status'] == 1)
		{
			if(empty($statusExist))
			{
				$saveData = array('event_id'=>$requestData['event_id'],'user_id'=>$requestData['user_id'],'current_status'=>$requestData['status']);
				$insertStatus = $CI->api_model->create('event_user_status',$saveData);

				if(!empty($insertStatus))
				{	

					$returnData['status'] = 0;
					$returnData['eventstatus'] = 1;
					$returnData['account_status'] = $userStatus;
					$returnData['data'] = array();
					$returnData['message'] = 'Event sharing status is inserted.';
					return $returnData;	
				}

			}
			else
			{
				$updateData = array('current_status'=>$requestData['status'],'updated_on' => date("Y-m-d H:i:s"));
				$updateCondition = array('id'=>$statusExist[0]['id']);
				$updateStatus = $CI->api_model->update('event_user_status',$updateCondition,$updateData);
				if(!empty($updateStatus))
				{
					$returnData['status'] = 0;
					$returnData['eventstatus'] = 1;
					$returnData['account_status'] = $userStatus;
					$returnData['data'] = array();
					$returnData['message'] = 'Event sharing status is updated.';
					return $returnData;
				}

			}
		}
		else
		{
			$returnData['status'] = 0;
			$returnData['eventstatus'] = '-1'; // Event deleted
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Event no longer exists.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function getEventDetail($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['event_id']) && isset($requestData['user_id']))
	{
		$condition = array('id' => $requestData['event_id']);
		$eventData = $CI->api_model->selectAllData('society_events', $condition);
		$eventData[0]['delete_event'] = '';
		$eventData[0]['user_response'] = '';
		if(!empty($eventData))
		{
			$eventData[0]['event_image_url'] = '';
			if(!empty($eventData[0]['event_image']))
			{
				$eventData[0]['event_image_url'] = $CI->config->item('uploaded_image_url').'images/event_images/'.$eventData[0]['event_image'];
			}
			$eventData[0]['event_date_time'] = date("jS F, Y h:i A", strtotime($eventData[0]['event_date']));
			$eventData[0]['event_time'] = date("h:i A", strtotime($eventData[0]['event_date']));
			$eventData[0]['date'] = date("Y-m-d",strtotime($eventData[0]['event_date']));
			if($eventData[0]['created_by'] == $requestData['user_id'])
			{
				$eventData[0]['delete_event'] = 'enabled';	
			}
			$userCondition = array('id' => $eventData[0]['created_by']);
			$userData = $CI->api_model->selectAllData('app_users',$userCondition);

			if(!empty($userData))
			{
				$eventData[0]['user_name'] = $userData[0]['name'];
			}

			$statusCondition = array('event_id' => $eventData[0]['id'],'user_id' => $requestData['user_id']);
			$statusData = $CI->api_model->selectAllData('event_user_status',$statusCondition);
			if(!empty($statusData))
			{
				$eventData[0]['user_response'] = $statusData[0]['current_status'];
			}
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $eventData;
			$returnData['message'] = 'Event details are fetched successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Unable to fetch event details, please try again.';
			return $returnData;	
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function getEventResponse($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['event_id']) && isset($requestData['status']))
	{
		$condition = array('event_id' => $requestData['event_id'], 'current_status'=> $requestData['status']);
		$eventResponse = $CI->api_model->selectAllData('event_user_status',$condition);

		if(!empty($eventResponse))
		{
			foreach ($eventResponse as $key => $value) 
			{
				$userCondition = array('id' => $value['user_id']);
				$userData = $CI->api_model->selectAllData('app_users', $userCondition);
				$userList[]= $userData[0];
				$userList[$key]['profile_image_url'] = '';
				if(!empty($userData[0]['profile_image']))
				{
					$userList[$key]['profile_image_url'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$userData[0]['profile_image']; 
				}
			}

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $userList;
			$returnData['message'] = 'The user list is fetched successfully.';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'No user found.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}	
}

function addNotification($userid, $societyid, $message, $eventid, $eventtype)
{
	$CI = get_instance();

	if(!empty($userid) && !empty($societyid) && !empty($message) && !empty($eventid) && !empty($eventtype))
	{
		$saveData = array(
			'user_id' => $userid,
			'society_id' => $societyid,
			'message' => $message,
			'event_id' => $eventid,
			'event_type' => $eventtype
			
			);
		$saveNotification = $CI->api_model->create('user_notifications',$saveData);

		return $saveNotification;
	}
	else
	{
		return 'Unable to save notification.';

	}
}

function getUserNotification($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);

	if(isset($requestData['user_id']))
	{	
		$condition = '(un.user_id='.$requestData['user_id'].') AND un.created_on >= DATE_ADD(CURDATE(), INTERVAL -2 DAY) ORDER BY un.created_on DESC';
		$notificationData = $CI->api_model->getNotification($condition);
		// $notificationData = $CI->api_model->selectAllData('user_notifications',$condition);
		if(!empty($notificationData))
		{
			foreach ($notificationData as $key => $value) 
			{
				$msgExplode = explode(" | ",$notificationData[$key]['message']);
				$notificationData[$key]['user_name'] = $msgExplode[0];
				$notificationData[$key]['notification_message'] = $msgExplode[1];
				$notificationData[$key]['date'] = date("jS F, Y", strtotime($notificationData[$key]['created_on']));
				$notificationData[$key]['time'] = date("h:i A", strtotime($notificationData[$key]['created_on']));
			}

			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $notificationData;
			$returnData['message'] = 'User notification is fetched successfully.';
			return $returnData;	
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'No user notification found.';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}

function userCurrentStatus($userid)
{
	$CI =& get_instance();
	if (isset($userid)) 
	{
		$condition = array('id'=> $userid);
		$currentStatus = $CI->api_model->selectAllData('app_users',$condition);

		if(!empty($currentStatus))
		{
			$userStatus = $currentStatus[0]['status'];
			return $userStatus;
		}
		else
		{
			return 0;
		}
	}
}

function pushNotification($userid)
{
	$CI =& get_instance();

	if(isset($userid))
	{
		$condition = array('id'=> $userid);
		$device_token = $CI->api_model->selectAllData('app_users',$condition);

		$regid = isset($device_token[0]['device_token']) ? $device_token[0]['device_token'] : "";	
		$api_access_key = $CI->config->item('api_access_key');
		$message = array(
			'message' => 'Test notification message.',
			'title' => 'Society Notification',
			'vibrate'   => 1,
			'sound' => 1,
			'largeIcon' => 'large_icon',
			'smallIcon' => 'small_icon'
			);
		$CI->api_model->push_notification($regid,$api_access_key,$message);        
	}
	else
	{
		return 'Unable to push notification.';
	}
}

function getUserSocietyPost($requestData)
{
	$CI =& get_instance();

	$userStatus = userCurrentStatus($requestData['user_id']);
	if(isset($requestData['user_id']) && isset($requestData['society_id']))
	{
		$userCondition = array('society_id' => $requestData['society_id'],'user_id' => $requestData['user_id'],'status' => 1);
		$userPost = $CI->api_model->selectAllData('user_posts', $userCondition);
		
		if(!empty($userPost))
		{
			foreach ($userPost as $key => $uvalue) 
			{
				$nowDate = date("Y-m-d H:i:s");
				$PostDate = $userPost[$key]['created_on'];
				$userPost[$key]['days_pass'] = time_elapsed_string($PostDate);
				if(!empty($userPost[$key]['post_image']))
				{
					$userPost[$key]['post_image_url'] = $CI->config->item('uploaded_image_url').'images/post_images/'.$userPost[$key]['post_image'];
				}
				else
				{
					$userPost[$key]['post_image_url'] = '';
				}

				$condition = array('uc.post_id' => $userPost[$key]['id'],'uc.status'=> 1);
				$commentData = $CI->api_model->getComments($condition);
				if(!empty($commentData))
				{
					foreach ($commentData as $ckey => $cvalue) 
					{
						$comment[$ckey]['id'] = $ckey;
						$comment[$ckey]['comment'] = $commentData[$ckey]['comment'];
						$comment[$ckey]['name'] = $commentData[$ckey]['name'];
						$comment[$ckey]['profile_image'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$commentData[$ckey]['profile_image'];
						$comment[$ckey]['user_id'] = $commentData[$ckey]['id']; 
					}
					$userPost[$key]['comments'] = $comment;
				}
				else
				{
					$userPost[$key]['comments'] = array(); 
				}

				$postCondition = array('id' => $userPost[$key]['user_id']);
				$userData = $CI->api_model->selectAllData('app_users',$postCondition);
				if (!empty($userData)) 
				{
					$userPost[$key]['created_by'] = $userData[0]['name'];
					$userPost[$key]['profile_image_url'] = '';
					if(!empty($userData[0]['profile_image']))
					{
						$userPost[$key]['profile_image_url'] = $CI->config->item('uploaded_image_url').'images/profile_images/'.$userData[0]['profile_image'];
					}
				}
				unset($comment);
			}
			$returnData['status'] = 0;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = $userPost;
			$returnData['message'] = 'View posts successfully';
			return $returnData;
		}
		else
		{
			$returnData['status'] = 1;
			$returnData['account_status'] = $userStatus;
			$returnData['data'] = array();
			$returnData['message'] = 'Post is not created';
			return $returnData;
		}
	}
	else
	{
		$returnData['status'] = 1;
		$returnData['account_status'] = $userStatus;
		$returnData['data'] = array();
		$returnData['message'] = 'Invalid or missing required parameters.';
		return $returnData;
	}
}


