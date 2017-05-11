<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class ApiController extends MY_Controller 
{
	public function __construct()
	{
		parent::__construct();

        $this->load->helper('Masterdata');
        $this->load->helper('otp');
        $this->load->model('api_model');
        // $userId = validate_jwt_and_user();
        // get_user_id_temp();
    }

    public function jwt_generate_token()
    {
        try
        {
            //set JWT header and payload data
            $jwt_header     = '{"alg":"HS256","typ":"JWT"}';
            $jwt_payload    = '{"userid":"10001","username":"guest"}';

            //encode header and paayload
            $jwt_header_encoded     = base64_encode($jwt_header);
            $jwt_payload_encoded    = base64_encode($jwt_payload);

            //concat header and payload, and encrypt
            $key_to_encode          = $jwt_header_encoded . "." . $jwt_payload_encoded;
            $server_hash            = hash_hmac("sha256", $key_to_encode, JWT_SECRETE_KEY, true);
            $server_hash_encoded    = base64_encode($server_hash);

            $response = $key_to_encode . "." . $server_hash_encoded;
            echo $response;
        }   
        catch(Exception $e)
        {
            $response_array['status']       = 0;
            $response_array['error_code']   = $this->error_code;
            $response_array['message']      = $e->getMessage();
            echo json_encode($response_array);
        }
    }

    public function register()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $registerUser = registerUser($request);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            if($registerUser['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $registerUser['status'];
                $responseArray['meta']['message'] = $registerUser['message'];
                $responseArray['data'] = $registerUser['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = $registerUser['status'];
                $responseArray['meta']['message'] = $registerUser['message'];
                $responseArray['data'] = $registerUser['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';         
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = ''; 
            echo json_encode($responseArray);
            exit;
        }
    }

    public function getCityList()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $skip = isset($request['skip']) ? $request['skip'] : 0;  
            $take = isset($request['take']) ? $request['take'] : 10;

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            if(isset($request['state_id']) && isset($request['country_id']))
            {
                $cityData = cityList($request['state_id'], $request['country_id'], $take, $skip);

                $responseArray['meta']['status_code'] = 0;
                $responseArray['meta']['message'] = '';
                $responseArray['data'] = $cityData;
                $responseArray['pagination']['page_size'] = count($cityData);
                $responseArray['pagination']['offset'] = $skip;        
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['message'] = 'Invalid Country Id or State Id';
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = $take;
                $responseArray['pagination']['offset'] = $skip;
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = $take;
            $responseArray['pagination']['offset'] = $skip;
            echo json_encode($responseArray);
            exit;
        }
    }

    public function getStateList()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $skip = isset($request['skip']) ? $request['skip'] : 0;  
            $take = isset($request['take']) ? $request['take'] : 10;

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            if(isset($request['country_id']))
            {
                $stateData = stateList($request['country_id'],$take,$skip);

                $responseArray['meta']['status_code'] = 0;
                $responseArray['meta']['message'] = '';
                $responseArray['data'] = $stateData;
                $responseArray['pagination']['page_size'] = count($stateData);
                $responseArray['pagination']['offset'] = $skip;        
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['message'] = 'Please provide valid inputs';
                $responseArray['data'] = [];
                $responseArray['pagination']['page_size'] = $take;
                $responseArray['pagination']['offset'] = $skip;        
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = [];
            $responseArray['pagination']['page_size'] = $take;
            $responseArray['pagination']['offset'] = $skip;        
            echo json_encode($responseArray);
            exit;
        }
    }

    public function getCountryList()
    {
        try
        {
            $input   = file_get_contents("php://input");
            $request  = json_decode($input,true);

            $skip    = isset($request['skip']) ? $request['skip'] : 0;  
            $take    = isset($request['take']) ? $request['take'] : 10;

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $countryData = countryList($take,$skip);

            $responseArray['meta']['status_code'] = 0;
            $responseArray['meta']['message'] = '';
            $responseArray['data'] = $countryData;
            $responseArray['pagination']['page_size'] = count($countryData);
            $responseArray['pagination']['offset'] = $skip;        
            echo json_encode($responseArray);
            exit;
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = [];
            $responseArray['pagination']['page_size'] = $take;
            $responseArray['pagination']['offset'] = $skip;        
            echo json_encode($responseArray);
            exit;
        }
    }

    public function sendOtpToUser()
    {
        try
        {
            $input   = file_get_contents("php://input");
            $request  = json_decode($input,true);
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();
            $message = '';

            if(isset($request['contact_type']) && isset($request['contact_detail']))
            {
                $checkContact = verifyContact($request['contact_type'],$request['contact_detail']);
                if($checkContact)
                {
                    $responseArray['meta']['status_code'] = 0;
                    $responseArray['meta']['message'] = 'Otp has been sent to Your '. $request['contact_type'];
                    $responseArray['data'] = array();
                    $responseArray['sender_id'] = 'VM-TESTMS';
                    $responseArray['pagination']['page_size'] = '';
                    $responseArray['pagination']['offset'] = '';        
                    echo json_encode($responseArray);
                    exit;
                }
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['message'] = 'Please Provide Valid Inputs';
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['message'] = 'Please Provide Valid Inputs';
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';        
            echo json_encode($responseArray);
            exit;
        }
    }

    public function verifyOtp()
    {
        try
        {
            $input   = file_get_contents("php://input");
            $request  = json_decode($input,true);
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            if(isset($request['contact_type']) && isset($request['otp']) && isset($request['contact_detail']))
            {
                $verifyOtp = verifyOtp($request['contact_type'],$request['contact_detail'],$request['otp']);
                if($verifyOtp){
                    $responseArray['meta']['status_code'] = 0;
                    $responseArray['meta']['message'] = 'OTP Verified!';
                    $responseArray['data'] = array();
                    $responseArray['pagination']['page_size'] = '';
                    $responseArray['pagination']['offset'] = '';        
                    echo json_encode($responseArray);
                    exit;
                }
                else{
                    $responseArray['meta']['status_code'] = 1;
                    $responseArray['meta']['message'] = 'Invalid Inputs';
                    $responseArray['data'] = array();
                    $responseArray['pagination']['page_size'] = '';
                    $responseArray['pagination']['offset'] = '';  
                    echo json_encode($responseArray);
                    exit;
                }
            }
            else{
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['message'] = 'Invalid Inputs';
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';  
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['status']    = 0;
            $responseArray['error_code']  = $this->error_code;
            $responseArray['message']   = $e->getMessage();
            echo json_encode($responseArray);
            exit;
        }
    }

    public function testOtp()
    {
        try
        {
            $input   = file_get_contents("php://input");
            $request  = json_decode($input,true);

            if(isset($request['contact_type']) && isset($request['contact_detail']))
            {
                $testOtp = testOtp($request['contact_type'],$request['contact_detail']);
                echo $testOtp;
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['status']    = 0;
            $responseArray['error_code']  = $this->error_code;
            $responseArray['message']   = $e->getMessage();
            echo json_encode($responseArray);
            exit;
        }
    }

    public function login()
    {
        $responseArray['meta'] = array();
        $responseArray['data'] = array();
        $responseArray['pagination'] = array();
        
        try
        {
            $input   = file_get_contents("php://input");
            $request  = json_decode($input,true);
            $responseArray = array();

            $verifyUser = loginUser($request);

            if($verifyUser['status'] == 0)
            {
                $responseArray['meta']['status_code'] = 0;
                $responseArray['meta']['message'] = $verifyUser['message'];
                $responseArray['data'] = $verifyUser['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['message'] = $verifyUser['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            
        }
        catch(Exception $e)
        {
            $responseArray['status']    = 1;
            $responseArray['error_code']  = $this->error_code;
            $responseArray['message']   = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit;
        }
    }

    public function editProfile()
    {
        try
        {
            $input   = file_get_contents("php://input");
            $request  = json_decode($input,true);
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $editUserProfile = editUser($request);

            if($editUserProfile['status'] == 0)
            {
                $responseArray['meta']['status_code'] = 0;
                $responseArray['meta']['account_status'] = 1;
                $responseArray['meta']['message'] = $editUserProfile['message'];
                $responseArray['data'] = $editUserProfile;
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = 1;
                $responseArray['meta']['message'] = $editUserProfile['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['status']    = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['error_code']  = $this->error_code;
            $responseArray['message']   = $e->getMessage();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit;
        }
    }

    public function forgotPassword()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $registerUser = forgotPasswordUser($request);
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            if($registerUser['status'] == 0)
            {
                $responseArray['meta']['status_code'] = 0;
                $responseArray['meta']['message'] = $registerUser['message'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['message'] =  $registerUser['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';         
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = ''; 
            echo json_encode($responseArray);
            exit;
        }
    }

    public function resetPassword()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $registerUser = resetPasswordUser($request);

            if($registerUser['status'] == 0)
            {
                $responseArray['meta']['status_code'] = 0;
                $responseArray['meta']['message'] = $registerUser['message'];
                $responseArray['data'] = $registerUser['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['message'] = $registerUser['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';         
                echo json_encode($responseArray);
                exit;

            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = ''; 
            echo json_encode($responseArray);
            exit;
        }
    }

    public function viewProfile()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $viewUser = getUserData($request);
            if($viewUser['status'] == 0)
            {
                $responseArray['meta']['status_code'] = 0;
                $responseArray['meta']['account_status'] = 1;
                $responseArray['meta']['message'] = $viewUser['message'];
                $responseArray['data'] = $viewUser['data'][0];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = ''; 
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = 1;
                $responseArray['meta']['message'] = $viewUser['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = ''; 
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = ''; 
            echo json_encode($responseArray);
            exit;
        }
    }

    public function registerSociety()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $registerSociety = registerSociety($request);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            if($registerSociety['status'] == 0)
            {
            	$responseArray['meta']['image_upload_status'] = $registerSociety['society_upload_message'];
                $responseArray['meta']['status_code'] = $registerSociety['status'];
                $responseArray['meta']['account_status'] = $registerSociety['account_status'];
                $responseArray['meta']['message'] = $registerSociety['message'];
                $responseArray['data'] = $registerSociety['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['image_upload_status'] = $registerSociety['society_upload_message'];
                $responseArray['meta']['status_code'] = $registerSociety['status'];
                $responseArray['meta']['account_status'] = $registerSociety['account_status'];
                $responseArray['meta']['message'] = $registerSociety['message'];
                $responseArray['data'] = $registerSociety['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';         
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 0;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = ''; 
            echo json_encode($responseArray);
            exit;
        }
    }

    public function searchSociety()
    {
    	try
    	{	
    		$input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $searchSociety = searchSociety($request);

            if($searchSociety['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $searchSociety['status'];
                $responseArray['meta']['account_status'] = $searchSociety['account_status'];
                $responseArray['meta']['message'] = $searchSociety['message'];
                $responseArray['data'] = $searchSociety['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;      
            }
            else
            {
                $responseArray['meta']['status_code'] = $searchSociety['status'];
                $responseArray['meta']['account_status'] = $searchSociety['account_status'];
                $responseArray['meta']['message'] = $searchSociety['message'];
                $responseArray['data'] = $searchSociety['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            
    	}
    	catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 0;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = ''; 
            echo json_encode($responseArray);
            exit;
        }
    }

    public function registerComplaint()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $userComplaint = userComplaint($request);

            if($userComplaint['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $userComplaint['status'];
                $responseArray['meta']['account_status'] = $userComplaint['account_status'];
                $responseArray['meta']['message'] = $userComplaint['message'];
                $responseArray['data'] = $userComplaint['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;     
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $userComplaint['account_status'];
                $responseArray['meta']['message'] = $userComplaint['message'];
                $responseArray['data'] = $userComplaint['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;     
            }

        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = ''; 
            echo json_encode($responseArray);
            exit;
        }
    }

    public function registerAnnouncements()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $userAnnouncement = userAnnouncement($request);

            if ($userAnnouncement['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $userAnnouncement['status'];
                $responseArray['meta']['account_status'] = $userAnnouncement['account_status'];
                $responseArray['meta']['message'] = $userAnnouncement['message'];
                $responseArray['data'] = $userAnnouncement['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $userAnnouncement['account_status'];
                $responseArray['meta']['message'] = $userAnnouncement['message'];
                $responseArray['data'] = $userAnnouncement['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit;
        }
    }

    public function getpropertyType()
    {
        try
        {
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $propertyType = getpropertyType();
            if(!empty($propertyType))
            {
                $responseArray['meta']['status_code'] = 0;
                $responseArray['meta']['account_status'] = 1;
                $responseArray['meta']['message'] = '';
                $responseArray['data'] = $propertyType;
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = 1;
                $responseArray['meta']['message'] = 'Property type is not available';
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit;
        }
    }

    public function registerProperty()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $registerProperty = registerUserProperty($request);

            if($registerProperty['status'] == 0)
            {
                $responseArray['meta']['image_upload_status'] = $registerProperty['property_upload_message'];
                $responseArray['meta']['status_code'] = $registerProperty['status'];
                $responseArray['meta']['account_status'] = 1;
                $responseArray['meta']['message'] = $registerProperty['message'];
                $responseArray['data'] = $registerProperty['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['image_upload_status'] = $registerProperty['property_upload_message'];
                $responseArray['meta']['status_code'] = $registerProperty['status'];
                $responseArray['meta']['account_status'] = $registerProperty['account_status'];
                $responseArray['meta']['message'] = $registerProperty['message'];
                $responseArray['data'] = $registerProperty['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';         
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = $registerProperty['account_status'];
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit;
        }
    }

    public function getUserAnnouncements()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $skip = isset($request['skip']) ? $request['skip'] : 0;
            $take = isset($request['take']) ? $request['take'] :10;

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $userGetAnnouncements = userGetAnnouncements($request);

            if($userGetAnnouncements['status'] == 0)
            {   

                $responseArray['meta']['status_code'] = $userGetAnnouncements['status'];
                $responseArray['meta']['account_status'] = $userGetAnnouncements['account_status'];
                $responseArray['meta']['message'] = $userGetAnnouncements['message'];
                $responseArray['data'] = $userGetAnnouncements['data'];
                $responseArray['pagination']['page_size'] = count($userGetAnnouncements);
                $responseArray['pagination']['offset'] = '$skip';        
                echo json_encode($responseArray);
                exit;     
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $userGetAnnouncements['account_status'];
                $responseArray['meta']['message'] = $userGetAnnouncements['message'];
                $responseArray['data'] = $userGetAnnouncements['data'];
                $responseArray['pagination']['page_size'] = '$take';
                $responseArray['pagination']['offset'] = '$skip';        
                echo json_encode($responseArray);
                exit;     
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = $userGetAnnouncements['account_status'];
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '$take';
            $responseArray['pagination']['offset'] = '$skip'; 
            echo json_encode($responseArray);
            exit;
        }
    } 

    public function deleteProperty()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $deleteProperty = deleteUserProperty($request);

            if($deleteProperty['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $deleteProperty['status'];
                $responseArray['meta']['account_status'] = $deleteProperty['account_status'];
                $responseArray['meta']['message'] = $deleteProperty['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = $deleteProperty['status'];
                $responseArray['meta']['account_status'] = $deleteProperty['account_status'];
                $responseArray['meta']['message'] = $deleteProperty['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';         
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit;
        }

    }

    public function getUserProperty()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $getProperty = getUserProperty($request);

            if($getProperty['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $getProperty['status'];
                $responseArray['meta']['account_status'] = $getProperty['account_status'];
                $responseArray['meta']['message'] = $getProperty['message'];
                $responseArray['data'] = $getProperty;
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = $getProperty['status'];
                $responseArray['meta']['account_status'] = $getProperty['account_status'];
                $responseArray['meta']['message'] = $getProperty['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';         
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit;
        }
    }

    public function editUserProperty()
    {
    	try
    	{
    		$input = file_get_contents("php://input");
            $request = json_decode($input,true);
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $editProperty = editUserProperty($request);

           	if($editProperty['status'] == 0)
           	{
           		$responseArray['meta']['status_code'] = 0;
                $responseArray['meta']['account_status'] = $editProperty['account_status'];
                $responseArray['meta']['message'] = $editProperty['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
           	}
           	else
           	{
           		$responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $editProperty['account_status'];
                $responseArray['meta']['message'] = $editProperty['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
           	}
    	}
    	catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit;
        }
    }   
  

    public function getSocietyAnnouncements()
    { 
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $skip = isset($request['skip']) ? $request['skip'] : 0;
            $take = isset($request['take']) ? $request['take'] : 10;

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $societyAnnouncements = societyAnnouncements($request);

            if ($societyAnnouncements['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $societyAnnouncements['status'];
                $responseArray['meta']['account_status'] = $societyAnnouncements['account_status'];
                $responseArray['meta']['message'] = $societyAnnouncements['message'];
                $responseArray['data'] = $societyAnnouncements['data'];
                $responseArray['pagination']['page_size'] = count($societyAnnouncements);
                $responseArray['pagination']['offset'] = '$skip';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $societyAnnouncements['account_status'];
                $responseArray['meta']['message'] = $societyAnnouncements['message'];
                $responseArray['data'] = $societyAnnouncements['data'];
                $responseArray['pagination']['page_size'] = '$take';
                $responseArray['pagination']['offset'] = '$skip';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '$take';
            $responseArray['pagination']['offset'] = '$skip';
            echo json_encode($responseArray);
            exit;
        }
    }

    public function getMySociety()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $getMySociety = getUserSociety($request);
            
            if ($getMySociety['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $getMySociety['status'];
                $responseArray['meta']['account_status'] = $getMySociety['account_status'];
                $responseArray['meta']['message'] = $getMySociety['message'];
                $responseArray['data'] = $getMySociety['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $getMySociety['account_status'];
                $responseArray['meta']['message'] = $getMySociety['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
           $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getSocietyMember()
    {
    	try
    	{
    		$input = file_get_contents("php://input");
    		$request = json_decode($input,true);

            $skip = isset($request['skip']) ? $request['skip'] : 0;
            $take = isset($request['take']) ? $request['take'] : 10;

    		$responseArray['meta'] = array();
    		$responseArray['data'] = array();
    		$responseArray['pagination'] = array();

    		$getMySocietyMember = getMySocietyMember($request);

    		if ($getMySocietyMember['status'] == 0) 
    		{
    			$responseArray['meta']['status_code'] = $getMySocietyMember['status'];
    			$responseArray['meta']['account_status'] = $getMySocietyMember['account_status'];
    			$responseArray['meta']['message'] = $getMySocietyMember['message'];
    			$responseArray['data'] = $getMySocietyMember['data'];
    			$responseArray['pagination']['page_size'] = count($getMySocietyMember);
    			$responseArray['pagination']['offset'] = '$skip';
    			echo json_encode($responseArray);
    			exit;    			
    		}
    		else
    		{
    			$responseArray['meta']['status_code'] = 1;
    			$responseArray['meta']['account_status'] = $getMySocietyMember['account_status'];
    			$responseArray['meta']['message'] = $getMySocietyMember['message'];
    			$responseArray['data'] = array();
    			$responseArray['pagination']['page_size'] = '$take';
    			$responseArray['pagination']['offset'] = '$skip';
    			echo json_encode($responseArray);
    			exit;
    		}
    	}
    	catch(Exception $e)
    	{
    		$responseArray['meta']['status_code'] = 1;
    		$responseArray['meta']['account_status'] =1;
    		$responseArray['meta']['message'] = $e->getMessage();
    		$responseArray['data'] = array();
    		$responseArray['pagination']['page_size'] = '$take';
    		$responseArray['pagination']['offset'] = '$skip';
    		echo json_encode($responseArray);
    		exit;
    	}
    }

    public function addUserPost()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $addPost = addUserPost($request);
            
            if ($addPost['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $addPost['status'];
                $responseArray['meta']['account_status'] = $addPost['account_status'];
                $responseArray['meta']['message'] = $addPost['message'];
                $responseArray['data'] = $addPost['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $addPost['account_status'];
                $responseArray['meta']['message'] = $addPost['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
           $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getPostBySociety()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $getPost = getPostBySociety($request);
            
            if ($getPost['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $getPost['status'];
                $responseArray['meta']['account_status'] = $getPost['account_status'];
                $responseArray['meta']['message'] = $getPost['message'];
                $responseArray['data'] = $getPost['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $getPost['account_status'];
                $responseArray['meta']['message'] = $getPost['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
           $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getPostByUser()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $getPost = getPostByUser($request);
            
            if ($getPost['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $getPost['status'];
                $responseArray['meta']['account_status'] = $getPost['account_status'];
                $responseArray['meta']['message'] = $getPost['message'];
                $responseArray['data'] = $getPost['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $getPost['account_status'];
                $responseArray['meta']['message'] = $getPost['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function addPostComment()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $addComment = addPostComment($request);
            
            if ($addComment['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $addComment['status'];
                $responseArray['meta']['account_status'] = $addComment['account_status'];
                $responseArray['meta']['message'] = $addComment['message'];
                $responseArray['data'] = $addComment['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $addComment['account_status'];
                $responseArray['meta']['message'] = $addComment['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function addUserPoll()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();
            $responseArray['meta']['share_message'] = '';

            $addPoll = addUserPoll($request);
            if ($addPoll['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $addPoll['status'];
                $responseArray['meta']['account_status'] = $addPoll['account_status'];
                $responseArray['meta']['message'] = $addPoll['message'];
                $responseArray['meta']['share_message'] = $addPoll['share_message'];
                $responseArray['data'] = $addPoll['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $addPoll['account_status'];
                $responseArray['meta']['message'] = $addPoll['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getUserPoll()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $getPoll = getUserPoll($request);
            if ($getPoll['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $getPoll['status'];
                $responseArray['meta']['account_status'] = $getPoll['account_status'];
                $responseArray['meta']['message'] = $getPoll['message'];
                $responseArray['data'] = $getPoll['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $getPoll['account_status'];
                $responseArray['meta']['message'] = $getPoll['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function publishPollResult()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();
            $responseArray['meta']['share_message'] = '';

            $result = publishPollResult($request);
            if($result['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $result['status'];
                $responseArray['meta']['account_status'] = $result['account_status'];
                $responseArray['meta']['message'] = $result['message'];
                $responseArray['data'] = $result['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $result['account_status'];
                $responseArray['meta']['message'] = $result['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getArchivePoll()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $archivePoll = getArchivePoll($request);
            if ($archivePoll['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $archivePoll['status'];
                $responseArray['meta']['account_status'] = $archivePoll['account_status'];
                $responseArray['meta']['message'] = $archivePoll['message'];
                $responseArray['data'] = $archivePoll['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $archivePoll['account_status'];
                $responseArray['meta']['message'] = $archivePoll['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function editEndDate()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $editDate = editEndDate($request);
            if ($editDate['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $editDate['status'];
                $responseArray['meta']['account_status'] = $editDate['account_status'];
                $responseArray['meta']['message'] = $editDate['message'];
                $responseArray['data'] = $editDate['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $editDate['account_status'];
                $responseArray['meta']['message'] = $editDate['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function archivePoll()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $archivePoll = archivePoll($request);
            if ($archivePoll['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $archivePoll['status'];
                $responseArray['meta']['account_status'] = $archivePoll['account_status'];
                $responseArray['meta']['message'] = $archivePoll['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $archivePoll['account_status'];
                $responseArray['meta']['message'] = $archivePoll['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function deletePoll()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $deletePoll = deletePoll($request);
            if ($deletePoll['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $deletePoll['status'];
                $responseArray['meta']['account_status'] = $deletePoll['account_status'];
                $responseArray['meta']['message'] = $deletePoll['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $deletePoll['account_status'];
                $responseArray['meta']['message'] = $deletePoll['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function addPollOption()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $addOption = addPollOption($request);
            if ($addOption['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $addOption['status'];
                $responseArray['meta']['pollstatus'] = $addOption['pollstatus'];
                $responseArray['meta']['account_status'] = $addOption['account_status'];
                $responseArray['meta']['message'] = $addOption['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['pollstatus'] = $addOption['pollstatus'];
                $responseArray['meta']['account_status'] = $addOption['account_status'];
                $responseArray['meta']['message'] = $addOption['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getPollDetails()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);
            
            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $pollDetails = getPollDetails($request);
            if ($pollDetails['status'] == 0) 
            {
                $responseArray['meta']['status_code'] = $pollDetails['status'];
                $responseArray['meta']['account_status'] = $pollDetails['account_status'];
                $responseArray['meta']['message'] = $pollDetails['message'];
                $responseArray['data'] = $pollDetails['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $pollDetails['account_status'];
                $responseArray['meta']['message'] = $pollDetails['message'];
                $responseArray['data'] =array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status']= 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    } 

    public function getOffersDetails()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $getOffers = getOffers($request);

            if($getOffers['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $getOffers['status'];
                $responseArray['meta']['account_status'] = 1;
                $responseArray['meta']['message'] = $getOffers['message'];
                $responseArray['data'] = $getOffers['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = 1;
                $responseArray['meta']['message'] = $getOffers['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function addSocietyUserGroups()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $createGroup = addSocietyUserGroups($request);

            if($createGroup['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $createGroup['status'];
                $responseArray['meta']['account_status'] = $createGroup['account_status'];
                $responseArray['meta']['message'] = $createGroup['message'];
                $responseArray['data'] = $createGroup['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $createGroup['account_status'];
                $responseArray['meta']['message'] = $createGroup['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            } 
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }

    }

    public function getSocietyUserGroups()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $showGroup = getSocietyUserGroups($request);

            if($showGroup['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $showGroup['status'];
                $responseArray['meta']['account_status'] = $showGroup['account_status'];
                $responseArray['meta']['message'] = $showGroup['message'];
                $responseArray['data'] = $showGroup['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $showGroup['account_status'];
                $responseArray['meta']['message'] = $showGroup['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getSocietyGroupMember()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $showMember = getSocietyGroupMember($request);

            if($showMember['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $showMember['status'];
                $responseArray['meta']['account_status'] = $showMember['account_status'];
                $responseArray['meta']['message'] = $showMember['message'];
                $responseArray['data'] = $showMember['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $showMember['account_status'];
                $responseArray['meta']['message'] = $showMember['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function deleteSocietyGroup()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $deleteGroup = deleteSocietyGroup($request);

            if($deleteGroup['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $deleteGroup['status'];
                $responseArray['meta']['account_status'] = $deleteGroup['account_status'];
                $responseArray['meta']['message'] = $deleteGroup['message'];
                $responseArray['data'] = $deleteGroup['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $deleteGroup['account_status'];
                $responseArray['meta']['message'] = $deleteGroup['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function createSocietyEvent()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $createEvent = createSocietyEvent($request);

            if($createEvent['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $createEvent['status'];
                $responseArray['meta']['account_status'] = $createEvent['account_status'];
                $responseArray['meta']['message'] = $createEvent['message'];
                $responseArray['data'] = $createEvent['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $createEvent['account_status'];
                $responseArray['meta']['message'] = $createEvent['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getSocietyEvent()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $getEvent = getSocietyEvent($request);

            if($getEvent['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $getEvent['status'];
                $responseArray['meta']['account_status'] = $getEvent['account_status'];
                $responseArray['meta']['message'] = $getEvent['message'];
                $responseArray['data'] = $getEvent['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $getEvent['account_status'];
                $responseArray['meta']['message'] = $getEvent['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getArchiveEvent()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $archiveEvent = getArchiveEvent($request);

            if($archiveEvent['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $archiveEvent['status'];
                $responseArray['meta']['account_status'] = $archiveEvent['account_status'];
                $responseArray['meta']['message'] = $archiveEvent['message'];
                $responseArray['data'] = $archiveEvent['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $archiveEvent['account_status'];
                $responseArray['meta']['message'] = $archiveEvent['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function deleteEvent()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $deleteEvent = deleteEvent($request);

            if($deleteEvent['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $deleteEvent['status'];
                $responseArray['meta']['account_status'] = $deleteEvent['account_status'];
                $responseArray['meta']['message'] = $deleteEvent['message'];
                $responseArray['data'] = $deleteEvent['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $deleteEvent['account_status'];
                $responseArray['meta']['message'] = $deleteEvent['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function submitEvent()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $submitEvent = submitEvent($request);

            if($submitEvent['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $submitEvent['status'];
                $responseArray['meta']['eventstatus'] = $submitEvent['eventstatus'];
                $responseArray['meta']['account_status'] = $submitEvent['account_status'];
                $responseArray['meta']['message'] = $submitEvent['message'];
                $responseArray['data'] = $submitEvent['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['eventstatus'] = $submitEvent['eventstatus'];
                $responseArray['meta']['account_status'] = $submitEvent['account_status'];
                $responseArray['meta']['message'] = $submitEvent['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getEventDetail()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $eventDetail = getEventDetail($request);

            if($eventDetail['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $eventDetail['status'];
                $responseArray['meta']['account_status'] = $eventDetail['account_status'];
                $responseArray['meta']['message'] = $eventDetail['message'];
                $responseArray['data'] = $eventDetail['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $eventDetail['account_status'];
                $responseArray['meta']['message'] = $eventDetail['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getEventResponse()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $eventResponse = getEventResponse($request);

            if($eventResponse['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $eventResponse['status'];
                $responseArray['meta']['account_status'] = $eventResponse['account_status'];
                $responseArray['meta']['message'] = $eventResponse['message'];
                $responseArray['data'] = $eventResponse['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $eventResponse['account_status'];
                $responseArray['meta']['message'] = $eventResponse['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getUserNotification()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $notificationResponse = getUserNotification($request);

            if($notificationResponse['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $notificationResponse['status'];
                $responseArray['meta']['account_status'] = $notificationResponse['account_status'];
                $responseArray['meta']['message'] = $notificationResponse['message'];
                $responseArray['data'] = $notificationResponse['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['account_status'] = $notificationResponse['account_status'];
                $responseArray['meta']['message'] = $notificationResponse['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';
                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = 1;
            $responseArray['meta']['account_status'] = 1;
            $responseArray['meta']['message'] = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit; 
        }
    }

    public function getUserSocietyPost()
    {
        try
        {
            $input = file_get_contents("php://input");
            $request = json_decode($input,true);

            $responseArray = array();
            $responseArray['meta'] = array();
            $responseArray['data'] = array();
            $responseArray['pagination'] = array();

            $getUserPost = getUserSocietyPost($request);

            if($getUserPost['status'] == 0)
            {
                $responseArray['meta']['status_code'] = $getUserPost['status'];
                $responseArray['meta']['account_status'] = $getUserPost['account_status'];
                $responseArray['meta']['message'] = $getUserPost['message'];
                $responseArray['data'] = $getUserPost['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';

                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = $getUserPost['status'];
                $responseArray['meta']['account_status'] = $getUserPost['account_status'];
                $responseArray['meta']['message'] = $getUserPost['message'];
                $responseArray['data'] = $getUserPost['data'];
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';

                echo json_encode($responseArray);
                exit;
            }
        }
        catch(Exception $e)
        {
            $responseArray['meta']['status_code'] = $getUserPost['status'];
            $responseArray['meta']['account_status'] = $getUserPost['account_status'];
            $responseArray['meta']['message'] = $getUserPost['data'];
            $responseArray['data'] = $getUserPost['data'];
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';

            echo json_encode($responseArray);
            exit;
        }
    }

    public function logout()
    {
        $responseArray['meta'] = array();
        $responseArray['data'] = array();
        $responseArray['pagination'] = array();
        
        try
        {
            $input   = file_get_contents("php://input");
            $request  = json_decode($input,true);
            $responseArray = array();

            $logoutUser = logoutUser($request);

            if($logoutUser['status'] == 0)
            {
                $responseArray['meta']['status_code'] = 0;
                $responseArray['meta']['message'] = $logoutUser['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            else
            {
                $responseArray['meta']['status_code'] = 1;
                $responseArray['meta']['message'] = $logoutUser['message'];
                $responseArray['data'] = array();
                $responseArray['pagination']['page_size'] = '';
                $responseArray['pagination']['offset'] = '';        
                echo json_encode($responseArray);
                exit;
            }
            
        }
        catch(Exception $e)
        {
            $responseArray['status']    = 1;
            $responseArray['error_code']  = $this->error_code;
            $responseArray['message']   = $e->getMessage();
            $responseArray['data'] = array();
            $responseArray['pagination']['page_size'] = '';
            $responseArray['pagination']['offset'] = '';
            echo json_encode($responseArray);
            exit;
        }
    }
}