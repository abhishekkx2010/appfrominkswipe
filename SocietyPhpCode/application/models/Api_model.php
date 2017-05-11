<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Api_model extends CI_Model 
{
    public function __construct()
    {
        parent::__construct();
        $this->error_code = "";
        $this->society = $this->load->database('default',true);
    }

   	public function encrypt_password($plain_password)
    {
        return hash('sha256', $plain_password);
    }

    public function select($table, $condition, $limit, $offset)
    {
        try
        {
            $this->society->select('*');
            $this->society->from($table);
            $this->society->where($condition);
            $this->society->limit($limit, $offset); 
            $result = $this->society->get()->result_array();
            return $result;
        }
        catch(Exception $e)
        {
            $response_array['status']       = 0;
            $response_array['error_code']   = $this->error_code;
            $response_array['message']      = $e->getMessage();
            echo json_encode($response_array);
        }
    }

    public function create($table, $save_data)
    {
        try
        {
            $this->society->insert($table, $save_data);
            return $this->society->affected_rows() > 0 ? $this->society->insert_id() : FALSE;
        }
        catch(Exception $e)
		{
			$response_array['status'] 		= 0;
			$response_array['error_code'] 	= $this->error_code;
			$response_array['message'] 		= $e->getMessage();
			echo json_encode($response_array);
		}
    }

    public function update($table, $condition, $update_data)
    {
        try
        {
            $this->society->where($condition);
            $this->society->update($table, $update_data);
            return $this->society->affected_rows() > 0 ? TRUE : FALSE;
        }
        catch(Exception $e)
		{
			$response_array['status'] 		= 0;
			$response_array['error_code'] 	= $this->error_code;
			$response_array['message'] 		= $e->getMessage();
			echo json_encode($response_array);
		}
    }
/***************************************************************************/

    public function selectAllData($table, $condition)
    {
        try
        {
            $this->society->select('*');
            $this->society->from($table);
            $this->society->where($condition);
            $result = $this->society->get()->result_array();
                return $result;
        }
        catch(Exception $e)
        {
            $response_array['status']       = 0;
            $response_array['error_code']   = $this->error_code;
            $response_array['message']      = $e->getMessage();
            echo json_encode($response_array);
        }
    }

     public function selectDistinctSociety($condition)
    {
        try
        {
            $this->society->distinct();
            $this->society->select('society_id');
            $this->society->from('user_properties');
            $this->society->where($condition);
            $result = $this->society->get()->result_array();
            return $result;
        }
        catch(Exception $e)
        {
            $response_array['status']       = 0;
            $response_array['error_code']   = $this->error_code;
            $response_array['message']      = $e->getMessage();
            echo json_encode($response_array);
        }

    }

    public function selectDistinctUser($condition)
    {
        try
        {
            $this->society->distinct();
            $this->society->select('user_id');
            $this->society->from('user_properties');
            $this->society->where($condition);
            $result = $this->society->get()->result_array();
            return $result;
        }
        catch(Exception $e)
        {
            $response_array['status'] = 0;
            $response_array['error_code'] = $this->error_code;
            $response_array['message'] = $e->getMessage();
            echo json_encode($response_array);

        }
    }

/***************************************************************************/


    public function getComments($condition)
    {
        try
        {
            $this->society->select('uc.comment,au.name,au.profile_image,au.id');
            $this->society->from('user_post_comments as uc');
            $this->society->join('app_users as au','au.id = uc.user_id');
            $this->society->where($condition);
            $this->society->order_by('created_on','DESC');
            $result = $this->society->get()->result_array();
            return $result;
        }
        catch(Exception $e)
        {
            $response_array['status'] = 0;
            $response_array['error_code'] = $this->error_code;
            $response_array['message'] = $e->getMessage();
            echo json_encode($response_array);

        }
    } 

     public function push_notification($regid,$api_access_key,$message)
    {
        $registrationIds = array( $regid );
        
        $fields = array(
        'registration_ids' => $registrationIds, // Device token
        'data'  => $message
        );
        
        $headers = array
        (
        'Authorization: key=' . $api_access_key, // API Access key
        'Content-Type: application/json'
        );
        
        $ch = curl_init();
        curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
        curl_setopt( $ch,CURLOPT_POST, true );
        curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
        curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
        curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
        curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
        $result = curl_exec($ch );
        curl_close( $ch );
        
    }

    public function getNotification($condition)
    {
        try
        {
            $this->society->select('un.*,s.name');
            $this->society->from('user_notifications un');
           $this->society->join('society s','s.id = un.society_id');
            
            $this->society->where($condition);         
            $result = $this->society->get()->result_array();
            return $result;
        }
        catch(Exception $e)
        {
             $responseArray['status']       = 0;
            $responseArray['error_code']   = $this->error_code;
            $responseArray['message']      = $e->getMessage();
            echo json_encode($responseArray);
        }
    }

    public function getSocietyMember($condition)
    {
        try
        {
            $this->society->select('p.user_id, u.name,u.profile_image');
            $this->society->from('user_properties p');
            $this->society->where('p.society_id', $condition['society_id']);
            $this->society->where('p.status', $condition['status']);
            $this->society->join('app_users u', 'p.user_id = u.id');
            $this->society->group_by('p.user_id');
            $result = $this->society->get()->result_array();

            $data = array();
            $response = array();
            if(!empty($result))
            {
                foreach ($result as $key => $value) {
                    $data['user_id'] = $value['user_id'];
                    $data['name'] = $value['name'];
                    $data['profile_image'] = $value['profile_image'];

                    $subdata = $this->getSocietyMemberSubData($value['user_id'], $condition['society_id']);

                    $property_name_array = array();
                    $property_type_array = array();
                    $house_no_array = array();

                    if(!empty($subdata))
                    {
                        foreach ($subdata as $key1 => $row) {
                            if(!empty($row['property_name']))
                            {
                                array_push($property_name_array, $row['property_name']);
                            }

                            if(!empty($row['property_type']))
                            {
                                array_push($property_type_array, $row['property_type']);
                            }

                            if(!empty($row['house_no']))
                            {
                                array_push($house_no_array, $row['house_no']);
                            }
                        }
                    }

                    $data['property_name_array'] = $property_name_array;
                    $data['property_type_array'] = $property_type_array;
                    $data['house_no_array'] = $house_no_array;

                    $response[] = $data;
                }

            }
            return $response;
        }
        catch(Exception $e)
        {
             $responseArray['status']       = 0;
            $responseArray['error_code']   = $this->error_code;
            $responseArray['message']      = $e->getMessage();
            echo json_encode($responseArray);
        }
    }

    public function getSocietyUserDetails($condition)
    {
        try
        {
            $this->society->select('s.name as society_name, s.pincode');
            $this->society->from('society s');
            $this->society->where('s.id', $condition['society_id']);
            $result = $this->society->get()->result_array();

            $returndata = array();
            foreach ($result as $key => $value) {
                $data['society_name'] = $value['society_name'];
                $data['pincode'] = $value['pincode'];

                $userdata = $this->getUserDetails($condition);
                $data['user_name'] = $userdata[0]['user_name'];

                $returndata[] = $data;
            }
            return $returndata;
        }
        catch(Exception $e)
        {
             $responseArray['status']       = 0;
            $responseArray['error_code']   = $this->error_code;
            $responseArray['message']      = $e->getMessage();
            echo json_encode($responseArray);
        }
    }

    public function getUserDetails($condition)
    {
        try
        {
            $this->society->select('a.name as user_name, a.device_token');
            $this->society->from('app_users a');
            $this->society->where('a.id', $condition['user_id']);
            $result = $this->society->get()->result_array();
            return $result;
        }
        catch(Exception $e)
        {
             $responseArray['status']       = 0;
            $responseArray['error_code']   = $this->error_code;
            $responseArray['message']      = $e->getMessage();
            echo json_encode($responseArray);
        }
    }

    public function getSocietyMemberSubData($user_id, $society_id)
    {
        try
        {
            $this->society->select('property_name, property_type, house_no');
            $this->society->from('user_properties');
            $this->society->where('user_id', $user_id);
            $this->society->where('society_id', $society_id);
            $result = $this->society->get()->result_array();
            return $result;
        }
        catch(Exception $e)
        {
            $responseArray['status']       = 0;
            $responseArray['error_code']   = $this->error_code;
            $responseArray['message']      = $e->getMessage();
            echo json_encode($responseArray);
        }
    }
}

