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
            $responseArray['status']       = 0;
            $responseArray['error_code']   = $this->error_code;
            $responseArray['message']      = $e->getMessage();
            echo json_encode($responseArray);
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
			$responseArray['status'] 		= 0;
			$responseArray['error_code'] 	= $this->error_code;
			$responseArray['message'] 		= $e->getMessage();
			echo json_encode($responseArray);
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
			$responseArray['status'] 		= 0;
			$responseArray['error_code'] 	= $this->error_code;
			$responseArray['message'] 		= $e->getMessage();
			echo json_encode($responseArray);
		}
    }

    public function deleteData($table, $condition)
    {
        try
        {
            $this->society->where($condition);
            $this->society->delete($table);
           
            return $this->society->affected_rows() > 0 ? TRUE : FALSE;
        }
        catch(Exception $e)
        {
            $response_array['status']       = 0;
            $response_array['error_code']   = $this->error_code;
            $response_array['message']      = $e->getMessage();
            echo json_encode($response_array);
        }
    }

    public function delete($table, $condition)
    {
        try
        {
            $delete_data = array('status' => -1);
            $this->society->where($condition);
            $this->society->update($table, $delete_data);
            return true;
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
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
            $this->society->order_by('id','DESC');
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

     public function selectState($table, $condition)
    {
        try
        {
            $this->society->select('*');
            $this->society->from($table);
            $this->society->where($condition);
            $this->society->order_by('state_name','ASC');
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

     public function selectCity($table, $condition)
    {
        try
        {
            $this->society->select('*');
            $this->society->from($table);
            $this->society->where($condition);
            $this->society->order_by('city_name','ASC');
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

/***************************************************************************/

    public function selectSociety($id) 
    {
        try
        {
            $this->society->select('soc.name as society_name,soc.city as society_city,soc.address as society_address,soc.state as society_state,soc.landmark as society_landmark,soc.area as society_area,soc.pincode as society_pincode,soc.post,soc.post_name,soc.society_image,soc.id as society_id,soc.user_id as user_id,u.*');
            $this->society->from('society_request soc');
            $this->society->join('app_users u','u.id = soc.user_id');
            $this->society->where('soc.id ='.$id);

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

    public function selectPrivateOffer($id)
    {
        try
        {
            // $this->society->select('off.shared_with ,off.title,off.description,off.company_name,off.company_address,off.contact_person_name,off.contact_person_mobile ,off.offer_image,off.offer_logo,off.created_by,off.created_on,off.status,off.expire_on');
            $this->society->select('off.*,off_map.*,soc.*,off.created_on as offer_created_on,off.id as offer_id,soc.id as society_id, off.status as offer_status,off.created_by as offer_created_by,off.category as offer_category,off.url as offer_url,soc.status as society_status,soc.created_by as society_created_by');
            $this->society->from('offers_master off');
            $this->society->join('offer_mapping off_map','off.id = off_map.offer_id');
            $this->society->join('society soc','soc.id = off_map.society_id');
            $this->society->where('off.id =' .$id);         
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

    public function selectPublicOffer($id)
    {
        try
        {
            $this->society->select('off.*,soc.*,off.created_on as offer_created_on,off.id as offer_id,soc.id as society_id,off.status as offer_status,off.category as offer_category,off.url as offer_url,off.created_by as offer_created_by,soc.status as society_status,soc.created_by as society_created_by');
            $this->society->from('offers_master off');
            $this->society->from('society soc');
            
            $this->society->where('off.id =' .$id);         
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

    public function selectsocietyProperty($condition)
    {
        try
        {
            $this->society->select('up.*,s.name as society_name,s.address as society_address,s.landmark as society_landmark,s.area as society_area,s.city as society_city,s.pincode as society_pincode,s.state as society_state,s.society_image as image_society');
            $this->society->from('user_properties up');
           $this->society->join('society s','s.id = up.society_id');
            
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

     public function selectcomplaint($condition)
    {
        try
        {
            $this->society->select('com.*,au.name as user_name,soc.name as society_name');
            $this->society->from('complaints com');
            $this->society->join('app_users au','au.id = com.created_by');
            $this->society->join('society soc','soc.id = com.society_id');
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
        return true;       
    } 
    public function userNotification($society_id)
    {
         try
        {
            $this->society->select('u.device_token');
            $this->society->from('society_request sr');
            $this->society->join('app_users u','u.id = sr.user_id');
            $this->society->where('sr.id',$society_id);
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

