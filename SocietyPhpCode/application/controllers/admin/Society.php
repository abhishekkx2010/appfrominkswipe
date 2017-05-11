<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Society extends Admin_Controller 
{
    public function __construct()
    {
        parent::__construct();

        $this->lang->load('admin/common');
        $this->load->helper('email_helper');
        $this->load->helper('alert_helper');
        $this->load->model('admin/Api_model');
        $this->load->library('pagination');
        $this->load->library('admin/DbTable');

        /* Title Page :: Common */
        // $this->page_title->push(lang('society_menu'));
        // $this->data['pagetitle'] = $this->page_title->show();
        $this->data['header_title'] = lang('society_menu');
        
        /* Breadcrumbs :: Common */
        // $this->breadcrumbs->unshift(1, lang('society_menu'), 'admin/society');

        if(!$this->ion_auth->logged_in() OR ! $this->ion_auth->is_admin())
        {
            redirect('auth/login', 'refresh');
        }
    }

   public function index()
   {  
         $this->page_title->push(lang('society_view'));
         $this->data['pagetitle'] = $this->page_title->show();
         $this->breadcrumbs->unshift(1, lang('society_view'), 'admin/society');
      try
      {
            $offset = ($this->uri->segment(4) != '' ? $this->uri->segment(4): 0);
            $condition = 'status IN(0,1)';
            $societies = $this->Api_model->selectAllData('society', $condition);
            
            $config['total_rows']       = count($societies);
            $config['per_page']         = 10;
            $config['base_url']         = base_url()."admin/society/index/"; 
            
            $this->pagination->initialize($config);

            $this->data['count']           = ($this->uri->segment(4) != "" ? $this->uri->segment(4) : '0') + 1;
            $this->data['start']           = $offset;
            $this->data['offset']          = $config['per_page'];

            $this->data['societies']    = $societies;
           $this->data['breadcrumb']      = $this->breadcrumbs->show();
          
           $this->template->admin_render('admin/society/index', $this->data);
        }
      catch(Exception $e)
      {
         $this->data['error'] = $e->getMessage();
         $this->template->admin_render('admin/error', $this->data);
      }
    }

    public function societyRequest()
    {   
        $this->page_title->push(lang('society_menu'));
        $this->data['pagetitle'] = $this->page_title->show();
        $this->breadcrumbs->unshift(1, lang('society_menu'), 'admin/society');
        try
        {
            $condition = array('status' => 1);
            $societies = $this->Api_model->selectAllData('society_request', $condition);
            $this->data['societies']    = $societies;
            $this->data['breadcrumb']      = $this->breadcrumbs->show();
           
            $this->template->admin_render('admin/society/societyrequest', $this->data);
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function create()
    {   
        $this->page_title->push(lang('society_create'));
        $this->data['pagetitle'] = $this->page_title->show();
        $this->breadcrumbs->unshift(1, lang('society_create'), 'admin/society');
        try
        {
            $stateCondition = array('country_id'=> 1); 
            $stateList = $this->Api_model->selectAllData('state_master',$stateCondition);

            /* Breadcrumbs */
            $this->breadcrumbs->unshift(2, lang('society_add_new'), 'admin/society/create');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

            if(!empty($_POST))
            {   
                if(isset($this->session->userdata['user_id']))
                {
                    $userId = $this->session->userdata['user_id'];
                    $condition = array('id' => $userId);
                    $user = $this->Api_model->selectAllData('users',$condition);
                }
                 $this->form_validation->set_rules('society_name', 'lang:society_name', 'required');
                 $this->form_validation->set_rules('society_adress', 'lang:society_address', 'required');
                 $this->form_validation->set_rules('society_landmark', 'lang:society_landmark', 'required');
                 $this->form_validation->set_rules('society_area', 'lang:society_area', 'required');
                 $this->form_validation->set_rules('society_state', 'lang:society_state', 'required');
                 $this->form_validation->set_rules('society_city', 'lang:society_city', 'required');
                 $this->form_validation->set_rules('society_pincode', 'lang:society_pincode', 'required');

                if ($this->form_validation->run() == TRUE)
                {
                    $directoryPath = $this->config->item('image_upload_path');

                    $directotyPathArray   = array('images', 'society_images');

                    foreach ($directotyPathArray as $key => $value) 
                    {
                        $directoryPath = $directoryPath.$value.'/';
                        if (!is_dir($directoryPath)) 
                        {
                            mkdir($directoryPath,0777);
                        }
                    }

                    $this->load->library('Upload_file');
                    $uploadResponse = $this->upload_file->upload_image('society_image', $directoryPath);
                    
                    if($uploadResponse == 1)
                    {
                        $uploadFileData = $this->upload->data();
                        $saveData = array(
                            'name' => $_POST['society_name'],
                            'user_id' => $userId,
                            'society_image'  => $uploadFileData['file_name'],
                            'address' => $_POST['society_address'],
                            'landmark' => $_POST['society_landmark'],
                            'area' => $_POST['society_area'],
                            'state' => $_POST['society_state'],
                            'pincode' => $_POST['society_pincode'],
                            'city' => $_POST['society_city'],
                            'post' => $_POST['society_post_flag'],
                            'post_name' => $_POST['society_post'],
                            'created_by' => $user[0]['username'],
                            'date_created'=>date("Y-m-d H:i:s"),
                            'status'        => $_POST['status'],
                        );

                        $saveResult = $this->Api_model->create('society', $saveData);
                        set_alert("success", "Society added successfully");
                    }
                    else
                    {
                        set_alert("danger", $uploadResponse);
                    }
                    redirect(base_url()."admin/society");
                }
                else
                {
                    $this->data['state_list'] = $stateList;
                    $this->data['society_name'] = $this->input->post('society_name');
                    $this->data['society_image']  = $this->input->post('society_image');
                    $this->data['society_address']  = $this->input->post('society_address');
                    $this->data['society_landmark']  = $this->input->post('society_landmark');
                    $this->data['society_area']  = $this->input->post('society_area');
                    $this->data['society_pincode'] = $this->input->post('society_pincode');
                    $this->data['society_state']  = $this->input->post('society_state');
                    $this->data['society_city']  = $this->input->post('society_city');
                    $this->data['hold_post'] = $this->input->post('society_post_flag');
                    $this->data['post']  = $this->input->post('society_post');
                    $this->data['status'] = $this->input->post('status');
                    $this->data['created_by'] = $this->input->post('created_by');
                    $this->template->admin_render('admin/society/societies', $this->data);
                }
            }
            else
            {
                $this->data['state_list'] = $stateList;
                $this->data['society_name'] = '';
                $this->data['society_image'] = '';
                $this->data['society_address'] = '';
                $this->data['society_pincode'] = '';
                $this->data['society_landmark'] = '';
                $this->data['society_area'] = '';
                $this->data['society_state'] = '';
                $this->data['society_city'] = '';
                $this->data['hold_post'] = '';
                $this->data['post'] = '';
                $this->data['status'] = "1";
                $this->data['created_by'] = '';

                $this->template->admin_render('admin/society/societies', $this->data);
            }
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function edit($id)
    {   
        $this->page_title->push(lang('society_edit_society'));
        $this->data['pagetitle'] = $this->page_title->show();
        $this->breadcrumbs->unshift(1, lang('society_view'), 'admin/society');

        $stateCondition = array('country_id'=> 1); 
        $stateList = $this->Api_model->selectState('state_master',$stateCondition);
        try
        {

            $id = (int) $id;
            /* Breadcrumbs */
            $this->breadcrumbs->unshift(2, lang('society_edit_society'), 'admin/society/edit');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

            if(!empty($_POST))
            {
                $stateIdCondition = array('state_id' => $_POST['society_state']);
                $stateData = $this->Api_model->selectAllData('state_master',$stateIdCondition);
                $condition = array('id' => $id);
                $response = $this->Api_model->selectAllData('society', $condition); 
                define ("MAX_SIZE","110");  
                $userId = $this->session->userdata['user_id'];
                $this->form_validation->set_rules('society_name', 'lang:society_name', 'trim|required|max_length[100]');
                $this->form_validation->set_rules('society_address', 'lang:society_address', 'trim|required|max_length[100]');
                $this->form_validation->set_rules('society_landmark', 'lang:society_landmark', 'trim|required|max_length[50]');
                $this->form_validation->set_rules('society_area', 'lang:society_area', 'trim|required|max_length[100]');
                $this->form_validation->set_rules('society_city', 'lang:society_city', 'required');
                $this->form_validation->set_rules('society_state', 'lang:society_state', 'required');
                $this->form_validation->set_rules('society_pincode', 'lang:society_pincode', 'required');

                if ($this->form_validation->run() == TRUE)
                {
                    if(!empty($_FILES['society_image']['name']))
                    {
                        $directoryPath = $this->config->item('image_upload_path');

                        $directotyPathArray   = array('images', 'society_images');

                        foreach ($directotyPathArray as $key => $value) 
                        {
                            $directoryPath = $directoryPath.$value.'/';
                            if (!is_dir($directoryPath)) 
                            {
                                mkdir($directoryPath,0777);
                            }
                        }

                        $this->load->library('Upload_file');
                        if($_FILES['society_image']['size'] != 0)
                        {
                            $date = date("Y-m-d H:i:s");
                            $societyName =  md5($date . mt_rand(1, 99999));
                            $societyFileName = stripslashes($_FILES['society_image']['name']);
                            $socImgExtension = $this->getExtension($societyFileName);
                            $socImage = $societyName.'.'.$socImgExtension;
                            $societyImage=filesize($_FILES['society_image']['tmp_name']);

                            if ($societyImage < MAX_SIZE*1024)
                            {
                              set_alert("danger", "You have uploaded small size image");
                              redirect(base_url()."admin/society/index");
                            }

                            if($socImgExtension=="jpg" || $socImgExtension=="jpeg" )
                            {
                                $uploadedOfferFile = $_FILES['society_image']['tmp_name'];
                                $socitySrc = imagecreatefromjpeg($uploadedOfferFile);
                            }
                            else if($socImgExtension=="png")
                            {
                                $uploadedOfferFile = $_FILES['society_image']['tmp_name'];
                                $socitySrc = imagecreatefrompng($uploadedOfferFile);
                            }
                            else 
                            {
                                $socitySrc = imagecreatefromgif($uploadedOfferFile);
                            }

                            list($socWidth,$socHeight)=getimagesize($uploadedOfferFile);

                            $newSociWidth=800;
                            $newSocHeight=600;
                            $socityTmp=imagecreatetruecolor($newSociWidth,$newSocHeight);

                            imagecopyresampled($socityTmp,$socitySrc,0,0,0,0,$newSociWidth,$newSocHeight,

                            $socWidth,$socHeight);

                            $societyFileName = $directoryPath."/". $socImage;
                           
                            imagejpeg($socityTmp,$societyFileName,100);

                            imagedestroy($socitySrc);
                            imagedestroy($socityTmp);
                        }
                            $updateData = array(
                                'name' => $_POST['society_name'],
                                'user_id' => $response[0]['user_id'],
                                'society_image'  => $socImage,
                                'address' => $_POST['society_address'],
                                'landmark' => $_POST['society_landmark'],
                                'area' => $_POST['society_area'],
                                'state' => $stateData[0]['state_name'],
                                'pincode' => $_POST['society_pincode'],
                                'city' => $_POST['society_city'],
                                'post' => $_POST['society_post_flag'],
                                'post_name' => $_POST['society_post'],
                                'status'  => $_POST['status'],
                                'created_by' => $_POST['created_by']
                                );
                            $condition = array('id' => $id);
                            $updateResult = $this->Api_model->update('society', $condition, $updateData);

                            $updatePropertyCondition = array('society_id' => $id);
                            $updatePropertyData = array('status' => $_POST['status']);
                            $updatePropertyResult = $this->Api_model->update('user_properties', $updatePropertyCondition, $updatePropertyData);
                            set_alert("success", "Society Updated successfully");
                    }
                    else
                    {
                        $updateData = array(
                            'name' => $_POST['society_name'],
                            'user_id' => $response[0]['user_id'],
                            'address' => $_POST['society_address'],
                            'landmark' => $_POST['society_landmark'],
                            'area' => $_POST['society_area'],
                            'state' => $stateData[0]['state_name'],
                            'pincode' => $_POST['society_pincode'],
                            'city' => $_POST['society_city'],
                            'post' => $_POST['society_post_flag'],
                            'post_name' => $_POST['society_post'],
                            'status' => $_POST['status'],
                            'created_by' => $_POST['created_by']
                            );

                        $condition = array('id' => $id);

                        $updateResult = $this->Api_model->update('society', $condition, $updateData);

                        $updatePropertyCondition = array('society_id' => $id);
                        $updatePropertyData = array('status' => $_POST['status']);
                        $updatePropertyResult = $this->Api_model->update('user_properties', $updatePropertyCondition, $updatePropertyData);

                        set_alert("success", "Society Updated successfully");
                    }
                    redirect(base_url()."admin/society");
                }
                else
                {
                    $idCondition = array('state_name' => $this->input->post('society_state'));
                    $state_id = $this->Api_model->selectState('state_master',$idCondition);
                    if(!empty($state_id))
                    {
                        $this->data['state_id'] = $state_id[0]['state_id'];
                    }
                    $this->data['state_list'] = $stateList;
                    $this->data['society_name'] = $this->input->post('society_name');
                    $this->data['society_image'] = $this->input->post('society_image');
                    $this->data['society_address'] = $this->input->post('society_address');
                    $this->data['society_landmark'] = $this->input->post('society_landmark');
                    $this->data['society_area'] = $this->input->post('society_area');
                    $this->data['society_pincode'] = $this->input->post('society_pincode');
                    $this->data['society_state']  = $this->input->post('society_state');
                    $this->data['society_city'] = $this->input->post('society_city');
                    $this->data['hold_post'] = $this->input->post('society_post_flag');
                    $this->data['post']  = $this->input->post('society_post');
                    $this->data['status']  = $this->input->post('status');
                    $this->data['created_by']  = $this->input->post('created_by');


                    $this->template->admin_render('admin/society/societies', $this->data);
                }
            }
            else
            {
                $condition = array('id' => $id);
                $response = $this->Api_model->selectAllData('society', $condition);

                if(!empty($response))
                {
                    $idCondition = array('state_name' => $response[0]['state']);
                    $state_id = $this->Api_model->selectState('state_master',$idCondition);
                    if(!empty($state_id))
                    {
                        $this->data['state_id'] = $state_id[0]['state_id'];
                    }
                    $this->data['state_list'] = $stateList;
                    $this->data['society_name']  = $response[0]['name'];
                    $this->data['society_image']   = $response[0]['society_image'];
                    $this->data['society_address']  = $response[0]['address'];
                    $this->data['society_pincode']  = $response[0]['pincode'];
                    $this->data['society_landmark']   = $response[0]['landmark'];
                    $this->data['society_area']   = $response[0]['area'];
                    $this->data['society_state']  = $response[0]['state'];
                    $this->data['society_city']   = $response[0]['city'];
                    $this->data['hold_post']  = $response[0]['post'];
                    $this->data['post']   = $response[0]['post_name'];
                    $this->data['status']  = $response[0]['status'];
                    $this->data['created_by'] = $response[0]['created_by'];

                    $this->template->admin_render('admin/society/societies', $this->data);
                }
                else
                {
                    set_alert("danger", "Invalid society id provided");
                    redirect(base_url()."admin/society");
                }
            }
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function delete_society()
    {
        try
        {
            if(!empty($_POST['society_id']))
            {
                $condition = array('id' => $_POST['society_id']);
                $deleteSociety = $this->Api_model->delete('society',$condition);

                $condition = array('society_id' => $_POST['society_id']);
                $deleteSociety = $this->Api_model->delete('user_properties',$condition);                
                set_alert('success', "Society deleted successfully.");
            }
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function view($id)
    {   
        $this->page_title->push(lang('society_view_detail'));
        $this->data['pagetitle'] = $this->page_title->show();
        $this->breadcrumbs->unshift(1, lang('society_menu'), 'admin/society/societyRequest');
        try
        {   
            $this->breadcrumbs->unshift(2, lang('society_view_detail'), 'admin/society/view');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

            $id = (int) $id;           
            $this->data['societyData'] = $this->Api_model->selectSociety($id);
            $this->template->admin_render('admin/society/view', $this->data);
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function approve()
    {
        try
        {
            if(isset($_POST))
            {
                $condition = array('id' => $_POST['society_id']);
                $societyData = $this->Api_model->selectAllData('society_request',$condition);

                if(!empty($societyData))
                {
                    $saveData = array(
                        'name' => $societyData[0]['name'],
                        'address' => $societyData[0]['address'],
                        'landmark' => $societyData[0]['landmark'],
                        'area'     => $societyData[0]['area'],
                        'state' => $societyData[0]['state'],
                        'city' => $societyData[0]['city'],
                        'pincode' => $societyData[0]['pincode'],
                        'post' => $societyData[0]['post'],
                        'post_name' => $societyData[0]['post_name'],
                        'society_image' => $societyData[0]['society_image'],
                        'user_id' => $societyData[0]['user_id'],
                        'status' => $societyData[0]['status'],
                        'date_created' => date("Y-m-d H:i:s"),
                        'created_by' => $societyData[0]['created_by']
                        );


                        $saveResult = $this->Api_model->create('society', $saveData);
                        
                        if(!empty($saveResult))
                        {
                            $updateData = array('status'=> -1);
                            $updateCondition = array('id'=> $societyData[0]['id']);
                            $updateSociety = $this->Api_model->update('society_request',$updateCondition,$updateData);
                            $msg = 'Your add society request has been approved.';

                                $approveUser = array('id' => $societyData[0]['user_id']);
                                $approveData = $this->Api_model->selectAllData('app_users',$approveUser);
                                $regid = isset($approveData[0]['device_token']) ? $approveData[0]['device_token'] : "";
                                $api_access_key = $this->config->item('api_access_key');

                                $message = array('message' =>$msg,'society_id' =>$saveResult);
                               $this->Api_model->push_notification($regid,$api_access_key,$message);

                            set_alert('success', "Society Approved."); exit;
                        }
                        else
                        {
                            set_alert("failure", "Society Not Approved.") ; exit;
                        }
                }
            }
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function reject()
    {
        try
        {
            if(isset($_POST))
            {
                if(!empty($_POST['society_id']))
                {
                    $updateCondition = array('id' => $_POST['society_id']);
                    $updateData = array('status'=> -1);
                    $updateResult = $this->Api_model->update('society_request',$updateCondition,$updateData);
                    if(!empty($updateResult))
                    {   
                        set_alert("success","Society Rejected"); exit;
                    }
                    else
                    {
                        set_alert("failure","Please try again"); exit;
                    }
                }
            }
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }

    }

    public function offersview()
    {   
        $this->page_title->push(lang('offers_view'));
        $this->data['pagetitle'] = $this->page_title->show();
        try
        {   
            $this->breadcrumbs->unshift(2, lang('offers_view'), 'admin/society/offersview');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

            $offset = ($this->uri->segment(4) != '' ? $this->uri->segment(4): 0);
            $condition = 'status IN(0,1)';
            $offers = $this->Api_model->selectAllData('offers_master', $condition);

            $condition = array('id' => $offers[0]['created_by']);
            $userData = $this->Api_model->selectAllData('users', $condition);

            $this->data['usersData'] = $userData[0];
            
            $config['total_rows']       = count($offers);
            $config['per_page']         = 10;
            $config['base_url']         = base_url()."admin/society/offersview/"; 
            
            $this->pagination->initialize($config);

            $this->data['count']           = ($this->uri->segment(4) != "" ? $this->uri->segment(4) : '0') + 1;
            $this->data['start']           = $offset;
            $this->data['offset']          = $config['per_page'];

            $this->data['offers']          = $offers;
            $this->data['breadcrumb']      = $this->breadcrumbs->show();
           
            $this->template->admin_render('admin/society/offerview', $this->data);
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function createOffer()
    {  
        $this->page_title->push(lang('offers_register'));
        $this->data['pagetitle'] = $this->page_title->show();
        
        try
        {
            /* Breadcrumbs */
            $this->breadcrumbs->unshift(2, lang('offers_register'), 'admin/society/createOffer');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

            if(!empty($_POST))
            {  
                $uploadFileData = array();
                $uploadFileData1 = array();
                define ("MAX_SIZE","110");
                $userId = $this->session->userdata['user_id'];
                $this->form_validation->set_rules('shared_with', 'lang:offers_shared_with', 'required');
                $this->form_validation->set_rules('title', 'lang:offers_title', 'trim|required|max_length[100]');
                $this->form_validation->set_rules('description', 'lang:offers_discription', 'trim|required|max_length[500]');
                $this->form_validation->set_rules('company_name', 'lang:offers_company_name', 'trim|required|max_length[100]');
                $this->form_validation->set_rules('company_address', 'lang:offers_company_address', 'required');
                $this->form_validation->set_rules('contact_person_name', 'lang:offers_contact_person','trim|required|max_length[100]|regex_match[/^[a-zA-Z\s]+$/]');
                $this->form_validation->set_rules('contact_person_mobile','lang:offers_contact_person_mobile','trim|required|regex_match[/^[789][0-9]{9}$/]','required');
                $this->form_validation->set_rules('contact_person_email','lang:offers_contact_person_email','trim|required|regex_match[/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,})$/]|valid_email');
                $this->form_validation->set_rules('status','lang:offers_status','required');
                $this->form_validation->set_rules('start_date','lang:offers_start_date','required');
                $this->form_validation->set_rules('expire_on','lang:offers_expiry','required');

                if ($this->form_validation->run() == TRUE)
                {
                    
                    $directoryPath = $this->config->item('image_upload_path');

                    $directotyPathArray   = array('images', 'offer_images');

                    foreach ($directotyPathArray as $key => $value) 
                    {
                        $directoryPath = $directoryPath.$value.'/';
                        if (!is_dir($directoryPath)) 
                        {
                            mkdir($directoryPath,0777);
                        }
                    }
                    $this->load->library('Upload_file');
                    if($_FILES['offer_logo']['size'] != 0)
                    {
                        $date = date("Y-m-d H:i:s");
                                $logoName =  md5($date . mt_rand(1, 99999));
                                $uploadLogoImage = stripslashes($_FILES['offer_logo']['name']);
                                $logoExtension = $this->getExtension($uploadLogoImage);
                                $offerLogo = $logoName.'.'.$logoExtension;
                                $size = filesize($_FILES['offer_logo']['tmp_name']);
                                if ($logoExtension == "jpg" || $logoExtension ="jpeg") 
                                {
                                    $uploadfile = $_FILES['offer_logo']['tmp_name'];
                                    $src = imagecreatefromjpeg($uploadfile);
                                }
                                else if($logoExtension == "png")
                                {
                                    $uploadOfferfile = $_FILES['offer_logo']['tmp_name'];
                                    $src = imagecreatefrompng($uploadfile);
                                }
                                else
                                {
                                    $src = imagecreatefromgif($uploadfile);
                                }
                                list($width,$height) = getimagesize($uploadfile);
                                $newwidth = 200;
                                $newheight = 200;

                                $tmp = imagecreatetruecolor($newwidth,$newheight);
                                imagecopyresampled($tmp,$src,0,0,0,0,$newwidth,$newheight,$width,$height);
                                $logoFileName = $directoryPath."/". $offerLogo;
                                imagejpeg($tmp,$logoFileName,100);
                                imagedestroy($src);
                                imagedestroy($tmp);
                    }

                    if($_FILES['offer_image']['size'] != 0)
                    {
                        $date = date("Y-m-d H:i:s");
                        $offerName =  md5($date . mt_rand(1, 99999));
                        $offerFileName = stripslashes($_FILES['offer_image']['name']);
                        $offerExtension = $this->getExtension($offerFileName);
                        $offerImage = $offerName.'.'.$offerExtension;
                        $offerSize=filesize($_FILES['offer_image']['tmp_name']);

                        if ($offerSize < MAX_SIZE*1024)
                        {
                          set_alert("danger", "You have uploaded small size image");
                          redirect(base_url()."admin/society/offersview");

                        }

                        if($offerExtension=="jpg" || $offerExtension=="jpeg" )
                        {
                            $uploadedOfferFile = $_FILES['offer_image']['tmp_name'];
                            $offerSrc = imagecreatefromjpeg($uploadedOfferFile);
                        }
                        else if($offerExtension=="png")
                        {
                            $uploadedOfferFile = $_FILES['offer_image']['tmp_name'];
                            $offerSrc = imagecreatefrompng($uploadedOfferFile);
                        }
                        else 
                        {
                            $offerSrc = imagecreatefromgif($uploadedOfferFile);
                        }
                       
                       list($offerWidth,$offerHeight)=getimagesize($uploadedOfferFile);

                        $newOfferWidth=800;
                        $newOfferHeight=600;
                        $offerTmp=imagecreatetruecolor($newOfferWidth,$newOfferHeight);

                        imagecopyresampled($offerTmp,$offerSrc,0,0,0,0,$newOfferWidth,$newOfferHeight,

                        $offerWidth,$offerHeight);

                        $offerFileName = $directoryPath."/". $offerImage;
                       
                        imagejpeg($offerTmp,$offerFileName,100);

                        imagedestroy($offerSrc);
                        imagedestroy($offerTmp);
                    }
                        $saveData = array(
                            'shared_with' => $_POST['shared_with'],
                            'title' => $_POST['title'],
                            'description' => $_POST['description'],
                            'category' => $_POST['category'],
                            'url' => $_POST['url'],
                            'company_name' => $_POST['company_name'],
                            'company_address' => $_POST['company_address'],
                            'offer_logo'  => $offerLogo,
                            'offer_image'  => $offerImage,
                            'contact_person_name' => $_POST['contact_person_name'],
                            'contact_person_mobile' => $_POST['contact_person_mobile'],
                            'contact_person_email' => $_POST['contact_person_email'],
                            'amount' => $_POST['amount'],
                            'creator_notes' => $_POST['creator_notes'],
                            'start_date' => $_POST['start_date'],
                            'created_by' => $userId,
                            'status'        => $_POST['status'],
                            'expire_on' =>$_POST['expire_on']
                        );
                        $saveResult = $this->Api_model->create('offers_master', $saveData);
                        if($_POST['shared_with'] == 'Private')
                        {
                            foreach ($_POST['society_id'] as $key => $value) 
                            {   
                                $data = array(
                                    'offer_id' => $saveResult,
                                    'society_id' =>$value,
                                    'created_by' => $userId,
                                    );
                            $saveOfferMapping = $this->Api_model->create('offer_mapping', $data ); 
                            }
                        }
                        set_alert("success", "Offer added successfully");
                        redirect(base_url()."admin/society/offersview");
                }
                else
                {
                    $condition = array('status' => 1);
                    $this->data['society_data'] = $this->Api_model->selectAllData('society', $condition);
                    $this->data['shared_with'] = '';
                    $this->data['offersTitle'] = '';
                    $this->data['description'] = '';
                    $this->data['category'] ='';
                    $this->data['url'] = '';
                    $this->data['company_name'] = '';
                    $this->data['company_address'] = '';
                    $this->data['offer_logo'] = '';
                    $this->data['offer_image'] = '';
                    $this->data['contact_person_name']  = '';
                    $this->data['contact_person_mobile']  = '';
                    $this->data['contact_person_email']  = '';
                    $this->data['amount'] = '';
                    $this->data['creator_notes'] = '';
                    $this->data['start_date'] = '';
                    $this->data['created_by'] = '';
                    $this->data['status'] = "1";
                    $this->data['expire_on'] = '';

                    $this->template->admin_render('admin/society/createoffers', $this->data);
                }
            }
            else
            {
                $condition = array('status' => 1);
                $this->data['society_data'] = $this->Api_model->selectAllData('society', $condition);
                $this->data['shared_with'] = '';
                $this->data['offersTitle'] = '';
                $this->data['description'] = '';
                $this->data['category'] ='';
                $this->data['url'] = '';
                $this->data['company_name'] = '';
                $this->data['company_address'] = '';
                $this->data['offer_logo'] = '';
                $this->data['offer_image'] = '';
                $this->data['contact_person_name']  = '';
                $this->data['contact_person_mobile']  = '';
                $this->data['contact_person_email']  = '';
                $this->data['amount'] = '';
                $this->data['creator_notes'] = '';
                $this->data['start_date'] = '';
                $this->data['created_by'] = '';
                $this->data['status'] = "1";
                $this->data['expire_on'] = '';

                $this->template->admin_render('admin/society/createoffers', $this->data);
            }
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

     public function delete_offer()
    {
        try
        {
            if(!empty($_POST['offer_id']))
            {
                $condition = array('id' => $_POST['offer_id']);
                $deleteSociety = $this->Api_model->delete('offers_master',$condition);
                set_alert('success', "Offer deleted successfully.");
            }
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

   public function editOffers($id)
    { 
        $this->page_title->push(lang('offers_edit'));
        $this->data['pagetitle'] = $this->page_title->show();
        $this->breadcrumbs->unshift(1, lang('offers_edit'), 'admin/society/createOffer');
        try
        {
            $id = (int) $id;
            $this->breadcrumbs->unshift(2, lang('centers_edit_center'), 'admin/society/editOffers');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

            if(!empty($_POST))
            {   
                $uploadFileData = array();
                $uploadFileData1 = array();
                define ("MAX_SIZE","110");
                $offerCondition = array('id' => $id);
                $response = $this->Api_model->selectAllData('offers_master', $offerCondition);
                $userId = $this->session->userdata['user_id'];
                $this->form_validation->set_rules('shared_with', 'lang:offers_shared_with', 'required');
                $this->form_validation->set_rules('title', 'lang:offers_title', 'trim|required|max_length[100]');
                $this->form_validation->set_rules('description', 'lang:offers_discription', 'trim|required|max_length[500]');
                $this->form_validation->set_rules('company_name', 'lang:offers_company_name', 'trim|required|max_length[100]');
                $this->form_validation->set_rules('company_address', 'lang:offers_company_address', 'required');
                $this->form_validation->set_rules('contact_person_name', 'lang:offers_contact_person','trim|required|max_length[100]');
                $this->form_validation->set_rules('contact_person_mobile','lang:offers_contact_person_mobile','trim|required|regex_match[/^[789][0-9]{9}$/]','required');
                $this->form_validation->set_rules('contact_person_email','lang:offers_contact_person_email','trim|required|regex_match[/^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,})$/]|valid_email');
                $this->form_validation->set_rules('status','lang:offers_status','required');
                $this->form_validation->set_rules('start_date','lang:offers_start_date','required');
                $this->form_validation->set_rules('expire_on','lang:offers_expiry','required');
                if ($this->form_validation->run() == TRUE)
                {
                    if(!empty($_FILES['offer_logo']['name'])  || !empty($_FILES['offer_image']['name']))
                    {
                        $directoryPath = $this->config->item('image_upload_path');

                        $directotyPathArray   = array('images', 'offer_images');

                        foreach ($directotyPathArray as $key => $value) 
                        {
                            $directoryPath = $directoryPath.$value.'/';
                            if (!is_dir($directoryPath)) 
                            {
                                mkdir($directoryPath,0777);
                            }
                        }

                        $this->load->library('Upload_file');
                        if($_FILES['offer_logo']['size'] != 0)
                            {
                                $date = date("Y-m-d H:i:s");
                                $logoName =  md5($date . mt_rand(1, 99999));
                                $uploadLogoImage = stripslashes($_FILES['offer_logo']['name']);
                                $logoExtension = $this->getExtension($uploadLogoImage);
                                $offerLogo = $logoName.'.'.$logoExtension;
                                $size = filesize($_FILES['offer_logo']['tmp_name']);
                                if ($logoExtension == "jpg" || $logoExtension ="jpeg") 
                                {
                                    $uploadfile = $_FILES['offer_logo']['tmp_name'];
                                    $src = imagecreatefromjpeg($uploadfile);
                                }
                                else if($logoExtension == "png")
                                {
                                    $uploadOfferfile = $_FILES['offer_logo']['tmp_name'];
                                    $src = imagecreatefrompng($uploadfile);
                                }
                                else
                                {
                                    $src = imagecreatefromgif($uploadfile);
                                }
                                list($width,$height) = getimagesize($uploadfile);
                                $newwidth = 200;
                                $newheight = 200;

                                $tmp = imagecreatetruecolor($newwidth,$newheight);
                                imagecopyresampled($tmp,$src,0,0,0,0,$newwidth,$newheight,$width,$height);
                                $logoFileName = $directoryPath."/". $offerLogo;
                                imagejpeg($tmp,$logoFileName,100);
                                imagedestroy($src);
                                imagedestroy($tmp);
                            }

                         if($_FILES['offer_image']['size'] != 0)
                            {
                                $date = date("Y-m-d H:i:s");
                                $offerName =  md5($date . mt_rand(1, 99999));
                                $uploadOfferImage = stripslashes($_FILES['offer_image']['name']);
                                $offerExtension = $this->getExtension($uploadOfferImage);
                                $offerImage = $offerName.'.'.$offerExtension;
                                $size = filesize($_FILES['offer_image']['tmp_name']);
                                if ($size < MAX_SIZE*1024)
                                {
                                  set_alert("danger", "You have uploaded small size image");
                                  redirect(base_url()."admin/society/offersview");

                                }
                                if ($offerExtension == "jpg" || $offerExtension ="jpeg") 
                                {
                                    $uploadOfferfile = $_FILES['offer_image']['tmp_name'];
                                    $src = imagecreatefromjpeg($uploadOfferfile);
                                }
                                else if($offerExtension == "png")
                                {
                                    $uploadOfferfile = $_FILES['offer_image']['tmp_name'];
                                    $src = imagecreatefrompng($uploadOfferfile);
                                }
                                else
                                {
                                    $src = imagecreatefromgif($uploadOfferfile);
                                }
                                list($width,$height) = getimagesize($uploadOfferfile);
                                $newwidth = 800;
                                $newheight = 600;

                                $tmp = imagecreatetruecolor($newwidth,$newheight);
                                imagecopyresampled($tmp,$src,0,0,0,0,$newwidth,$newheight,$width,$height);
                                $offerFileName = $directoryPath."/". $offerImage;
                                imagejpeg($tmp,$offerFileName,100);
                                imagedestroy($src);
                                imagedestroy($tmp);
                            }

                            if(empty($offerLogo))
                            {
                               $offerLogo = $response[0]['offer_logo'];
                            }
                            if(empty($offerImage))
                            {
                                $offerImage = $response[0]['offer_image'];
                            }

                            $updateData = array(
                            'shared_with' => $_POST['shared_with'],
                            'title' => $_POST['title'],
                            'description' => $_POST['description'],
                            'category' => $_POST['category'],
                            'url' => $_POST['url'],
                            'company_name' => $_POST['company_name'],
                            'company_address' => $_POST['company_address'],
                            'offer_logo'  => $offerLogo,
                            'offer_image'  => $offerImage,
                            'contact_person_name' => $_POST['contact_person_name'],
                            'contact_person_mobile' => $_POST['contact_person_mobile'],
                            'contact_person_email' => $_POST['contact_person_email'],
                            'amount' => $_POST['amount'],
                            'creator_notes' => $_POST['creator_notes'],
                            'start_date' => $_POST['start_date'],
                            'created_by' => $userId,
                            'status'        => $_POST['status'],
                            'expire_on' =>$_POST['expire_on']
                             );
                            $condition = array('id' => $id);
                            $updateResult = $this->Api_model->update('offers_master', $condition, $updateData);
                            if ($_POST['shared_with'] == 'Private') 
                            {   
                                $condition = array('offer_id' => $id);
                                $deleteOffer = $this->Api_model->deleteData('offer_mapping',$condition);

                                foreach ($_POST['society_id'] as $key => $value) 
                                {
                                    $dataMapping = array(
                                    'offer_id' => $id,
                                    'society_id' =>$value,
                                    'created_by' => $userId
                                    );
                                    $updateMappingData = $this->Api_model->create('offer_mapping', $dataMapping); 
                                }
                            }
                            elseif ($_POST['shared_with'] == 'Public') 
                            {
                               $condition = array('offer_id' => $id);
                               $publicData = $this->Api_model->selectAllData('offer_mapping', $condition);
                               if(!empty($publicData))
                               {
                                $condition = array('offer_id' => $id);
                                $publicOffer = $this->Api_model->deleteData('offer_mapping',$condition);
                               }
                            }
                            set_alert("success", "Offer updated successfully");
                    }
                    else
                    {
                        $updateData = array(
                            'shared_with' => $_POST['shared_with'],
                            'title' => $_POST['title'],
                            'description' => $_POST['description'],
                            'category' => $_POST['category'],
                            'url' => $_POST['url'],
                            'company_name' => $_POST['company_name'],
                            'company_address' => $_POST['company_address'],
                            'contact_person_name' => $_POST['contact_person_name'],
                            'contact_person_mobile' => $_POST['contact_person_mobile'],
                            'contact_person_email' => $_POST['contact_person_email'],
                            'amount' => $_POST['amount'],
                            'creator_notes' => $_POST['creator_notes'],
                            'start_date' => $_POST['start_date'],
                            'created_by' =>$userId,
                            'status'        => $_POST['status'],
                            'expire_on' =>$_POST['expire_on']
                            );
                        $condition = array('id' => $id);

                        $updateResult = $this->Api_model->update('offers_master', $condition, $updateData);
                        if ($_POST['shared_with'] == 'Private') 
                            {   
                                 $condition = array('offer_id' => $id);
                                $deleteOffer = $this->Api_model->deleteData('offer_mapping',$condition);

                                foreach ($_POST['society_id'] as $key => $value) 
                                {
                                    $Mapping = array(
                                    'offer_id' => $id,
                                    'society_id' =>$value,
                                    'created_by' => $userId
                                    );
                                    $updateMapping = $this->Api_model->create('offer_mapping',$Mapping); 
                                }
                            }
                            elseif ($_POST['shared_with'] == 'Public') 
                            {
                               $condition = array('offer_id' => $id);
                               $publicData = $this->Api_model->selectAllData('offer_mapping', $condition);
                               if(!empty($publicData))
                               {
                                $condition = array('offer_id' => $id);
                                $publicOffer = $this->Api_model->deleteData('offer_mapping',$condition);
                               }
                            }
                        set_alert("success", "Offer Updated successfully");
                    }
                    redirect(base_url()."admin/society/offersview");
                }
                else
                {
                    $condition = array('status' => 1);
                    $this->data['society_data'] = $this->Api_model->selectAllData('society', $condition);
                    $this->data['shared_with']  = $response[0]['shared_with'];
                    $this->data['offersTitle']   = $response[0]['title'];
                    $this->data['category'] = $response[0]['category'];
                    $this->data['url'] = $response[0]['url'];
                    $this->data['company_name'] = $response[0]['company_name'];
                    $this->data['company_address'] = $response[0]['company_address'];
                    $this->data['contact_person_name'] = $response[0]['contact_person_name'];
                    $this->data['contact_person_mobile'] = $response[0]['contact_person_mobile'];
                    $this->data['contact_person_email'] = $response[0]['contact_person_email'];
                    $this->data['creator_notes'] = $response[0]['creator_notes'];
                    $this->data['amount'] = $response[0]['amount'];
                    $this->data['start_date'] = $response[0]['start_date'];
                    $this->data['description']  = $response[0]['description'];
                    $this->data['offer_image']  = $response[0]['offer_image'];
                    $this->data['offer_logo']   = $response[0]['offer_logo'];
                    $this->data['created_by'] = $response[0]['created_by'];
                    $this->data['status']  = $response[0]['status'];
                    $this->data['expire_on'] = $response[0]['expire_on'];

                    $this->template->admin_render('admin/society/createoffers', $this->data);
                }
            }
            else
            {
                $condition = array('status' => 1);
                $this->data['society_data'] = $this->Api_model->selectAllData('society', $condition);
                $offerCondition = array('id' => $id);
                $response = $this->Api_model->selectAllData('offers_master', $offerCondition);

                if(!empty($response))
                {
                    $condition = array('offer_id' => $id);
                    $this->data['selected_society'] = $this->Api_model->selectAllData('offer_mapping', $condition);
                    $this->data['shared_with']  = $response[0]['shared_with'];
                    $this->data['offersTitle']   = $response[0]['title'];
                    $this->data['category'] = $response[0]['category'];
                    $this->data['url'] = $response[0]['url'];
                    $this->data['company_name'] = $response[0]['company_name'];
                    $this->data['company_address'] = $response[0]['company_address'];
                    $this->data['contact_person_name'] = $response[0]['contact_person_name'];
                    $this->data['contact_person_mobile'] = $response[0]['contact_person_mobile'];
                    $this->data['contact_person_email'] = $response[0]['contact_person_email'];
                    $this->data['creator_notes'] = $response[0]['creator_notes'];
                    $this->data['amount'] = $response[0]['amount'];
                    $this->data['start_date'] = $response[0]['start_date'];
                    $this->data['description']  = $response[0]['description'];
                    $this->data['offer_image']  = $response[0]['offer_image'];
                    $this->data['offer_logo']   = $response[0]['offer_logo'];
                    $this->data['created_by'] = $response[0]['created_by'];
                    $this->data['status']  = $response[0]['status'];
                    $this->data['expire_on'] = $response[0]['expire_on'];

                    $this->template->admin_render('admin/society/createoffers', $this->data);
                }
                else
                {
                    redirect(base_url()."admin/society/offersview");
                }
            }
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function viewOffersDetails($id)
    { 
        $this->page_title->push(lang('offers_view_offer'));
        $this->data['pagetitle'] = $this->page_title->show();
        $this->breadcrumbs->unshift(1, lang('offers_view'), 'admin/society/editOffers');
        try
        {  
            $this->breadcrumbs->unshift(2, lang('offers_view_offer'), 'admin/society/createOffer');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

            $id = (int) $id; 
            $condition = array('id' => $id);
            $offerData = $this->Api_model->selectAllData('offers_master',$condition);

            if($offerData[0]['shared_with'] == 'Private')
            {
              $this->data['offerData'] = $this->Api_model->selectPrivateOffer($id); 

            }
            else
            {
              $this->data['offerData'] = $this->Api_model->selectPublicOffer($id);   
            }
 
            if(!empty($offerData))
            { 
            $condition = array('id' =>  $this->data['offerData'][0]['offer_created_by']);         
            $users = $this->Api_model->selectAllData('users', $condition);
           
            }
            if(!empty($users))
            {
              $this->data['username'] = $users[0]['username'];
            }  
            
            $this->template->admin_render('admin/society/viewoffersdetails', $this->data);
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function appMembers()
    {
        $this->page_title->push(lang('app_members_details'));
        $this->data['pagetitle'] = $this->page_title->show();
        $this->breadcrumbs->unshift(1, lang('app_members_details'), 'admin/society/appMembers');
        try
        {   
            $offset = ($this->uri->segment(4) != '' ? $this->uri->segment(4): 0);
            $condition = 'status IN(0,1)';
            $allAppMembers = $this->Api_model->selectAllData('app_users', $condition);
            $config['total_rows']       = count($allAppMembers);
            $config['per_page']         = 10;
            $config['base_url']         = base_url()."admin/society/appMembers/"; 
            
            $this->pagination->initialize($config);

            $this->data['count']           = ($this->uri->segment(4) != "" ? $this->uri->segment(4) : '0') + 1;
            $this->data['start']           = $offset;
            $this->data['offset']          = $config['per_page'];

            $this->data['allAppMembers']   = $allAppMembers;
            $this->data['breadcrumb']      = $this->breadcrumbs->show();
           
            $this->template->admin_render('admin/society/appmembers', $this->data);
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function viewMember($id)
    {
        $this->page_title->push(lang('app_members'));
        $this->data['pagetitle'] = $this->page_title->show();
        $this->breadcrumbs->unshift(1, lang('app_members_details'), 'admin/society/appMembers');
        try
        {  
            $this->breadcrumbs->unshift(2, lang('app_members'), 'admin/society/viewMember');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

            $id = (int) $id; 
            $condition = array('id' => $id);
            $this->data['appUser']= $this->Api_model->selectAllData('app_users',$condition);

            $condition = 'up.user_id = '.$id.' AND up.status IN(0,1) AND s.status = 1';
            $this->data['user_properties']= $this->Api_model->selectsocietyProperty($condition);

            $this->template->admin_render('admin/society/viewmember', $this->data);
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function updateStatus()
    {
        try
        {
            if($_POST['status'] == 1) 
            {
               $updateData = array('status' => 0);
            }
            else
            {
                $updateData =  array('status' => 1);
            }
            $condition = array('id' => $_POST['user_id']);
            $updateStatus =  $this->Api_model->update('app_users', $condition, $updateData);
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }
    public function propertyStatus()
    {
        try
         {   
            
            if($_POST['status'] == 1) 
            {
               $propertyData = array('status' => 0);
            }
            else
            {
                $propertyData =  array('status' => 1);
            }
            $condition = array('id' => $_POST['property_id']);
            $propertyStatus =  $this->Api_model->update('user_properties', $condition, $propertyData);
            exit;
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function deleteProperty()
    {
        try
        {
            if(!empty($_POST['property_id']))
            {
                $condition = array('id' => $_POST['property_id']);
                $deleteProperty = $this->Api_model->delete('user_properties',$condition);
                set_alert('success', "Propertry deleted successfully.");
            }
            redirect(site_url()."admin/society");
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function viewSociety($id)
    {
        $this->page_title->push(lang('society_views'));
        $this->data['pagetitle'] = $this->page_title->show();
        $this->breadcrumbs->unshift(1, lang('society_view'), 'admin/society');
        try
        {  
            $this->breadcrumbs->unshift(2, lang('society_views'), 'admin/society/viewSociety');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

            $id = (int) $id; 
            $condition = array('id' => $id);
            $this->data['viewSociety']= $this->Api_model->selectAllData('society',$condition);

            $this->template->admin_render('admin/society/viewsociety', $this->data);
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function complaintList()
    {
       $this->page_title->push(lang('complaint'));
        $this->data['pagetitle'] = $this->page_title->show();
       try
       {
        $this->breadcrumbs->unshift(2, lang('complaint'), 'admin/society/complaintList');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

        $offset = ($this->uri->segment(4) != '' ? $this->uri->segment(4): 0);
        $condition = array('status' => 1);
        $complaint = $this->Api_model->selectAllData('complaints',$condition);
        $config['total_rows']       = count($complaint);
        $config['per_page']         = 10;
        $config['base_url']         = base_url()."admin/society/complaintList/"; 

        $this->pagination->initialize($config);

        $this->data['count']           = ($this->uri->segment(4) != "" ? $this->uri->segment(4) : '0') + 1;
            $this->data['start']           = $offset;
            $this->data['offset']          = $config['per_page'];

            $this->data['complaint']          = $complaint;
            $this->data['breadcrumb']      = $this->breadcrumbs->show();

            $this->template->admin_render('admin/society/complaintlist', $this->data);
       }
       catch(Exception $e)
       {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
       }
    }

    public function viewComplaint($id)
    {
        try
        {
            $userId = $this->session->userdata['user_id'];
            $id = (int) $id; 
            $this->page_title->push(lang('complaint_view'));
            $this->data['pagetitle'] = $this->page_title->show();
            $this->breadcrumbs->unshift(1, lang('complaint'), 'admin/society/complaintList');
            $this->breadcrumbs->unshift(2, lang('complaint_view'), 'admin/society/viewComplaint');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();
            if(empty($_POST))
            {                
                $condition = array('id' => $id);
                $this->data['userComplaint']= $this->Api_model->selectAllData('complaints',$condition);

                if(!empty($this->data['userComplaint']))
                {
                    $condition = array('com.id' => $id);
                    $this->data['userComplaint'] = $this->Api_model->selectcomplaint($condition);
                }
            }
            else
            {
                if(!empty($_POST['comments']))
                {
                    $condition = array('id' => $id);
                    $updateData = array(
                        'status' => 0,
                        'comments' => $_POST['comments'],
                        'processed_by' =>$userId,
                        'processed_on' => date("Y-m-d H:i:s")
                        );
                    $insertComplaint = $this->Api_model->update('complaints',$condition,$updateData);
                    set_alert("success","Complaint Closed");
                    redirect('admin/society/complaintList');
                }
                else
                {
                    set_alert("danger","Please enter comment");
                    redirect('admin/society/viewComplaint/'.$id);
                }
            }
            $this->template->admin_render('admin/society/viewcomplaint', $this->data);                
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function closeComplaint()
    {
        $this->page_title->push(lang('complaint_closelist'));
        $this->data['pagetitle'] = $this->page_title->show();
       try
       {
        $this->breadcrumbs->unshift(2, lang('complaint_closelist'), 'admin/society/complaintList');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

        $offset = ($this->uri->segment(4) != '' ? $this->uri->segment(4): 0);
        $condition = array('status' => 0);
        $closeComplaint = $this->Api_model->selectAllData('complaints',$condition);
        $config['total_rows']       = count($closeComplaint);
        $config['per_page']         = 10;
        $config['base_url']         = base_url()."admin/society/closeComplaint/"; 

        $this->pagination->initialize($config);

        $this->data['count']           = ($this->uri->segment(4) != "" ? $this->uri->segment(4) : '0') + 1;
            $this->data['start']           = $offset;
            $this->data['offset']          = $config['per_page'];

            $this->data['closeComplaint']          = $closeComplaint;
            $this->data['breadcrumb']      = $this->breadcrumbs->show();

            $this->template->admin_render('admin/society/closecomplaintlist', $this->data);
       }
       catch(Exception $e)
       {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
       }
    }

    public function viewCloseComplaint($id)
    {
        $this->page_title->push(lang('complaint_closed_view'));
        $this->data['pagetitle'] = $this->page_title->show();
        $this->breadcrumbs->unshift(1, lang('complaint_closelist'), 'admin/society/closeComplaint');
        try
        {  
            $this->breadcrumbs->unshift(2, lang('complaint_closed_view'), 'admin/society/viewCloseComplaint');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

            $id = (int) $id; 
            $condition = array('id' => $id);
            $this->data['viewComplaint']= $this->Api_model->selectAllData('complaints',$condition);

            if(!empty($this->data['viewComplaint']))
                {
                    $condition = array('com.id' => $id);
                    $this->data['viewComplaint'] = $this->Api_model->selectcomplaint($condition);
                }

            $usercondition = array('id' => $this->data['viewComplaint'][0]['processed_by']);
            $currentUser = $this->Api_model->selectAllData('users',$usercondition);
            $this->data['viewComplaint'][0]['username'] = $currentUser[0]['username'];

            $this->template->admin_render('admin/society/viewclosecomplaint', $this->data);
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    public function memberNotification()
    {
        $this->page_title->push(lang('Notification'));
        $this->data['pagetitle'] = $this->page_title->show();
        $this->breadcrumbs->unshift(1, lang('Notification_member'), 'admin/society/memberNotification');
        try
        {   
            $offset = ($this->uri->segment(4) != '' ? $this->uri->segment(4): 0);
            $condition = array('status' => 1);
            $Notifications = $this->Api_model->selectAllData('app_users', $condition);

            $config['total_rows']       = count($Notifications);
            $config['per_page']         = 10;
            $config['base_url']         = base_url()."admin/society/memberNotification/"; 
            
            $this->pagination->initialize($config);

            $this->data['count']           = ($this->uri->segment(4) != "" ? $this->uri->segment(4) : '0') + 1;
            $this->data['start']           = $offset;
            $this->data['offset']          = $config['per_page'];

            $this->data['Notifications']   = $Notifications;
            $this->data['breadcrumb']      = $this->breadcrumbs->show();
           
            $this->template->admin_render('admin/society/membersfornotification', $this->data);
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

   public function sendNotification()
   {
        $this->page_title->push(lang('Notification'));
        $this->data['pagetitle'] = $this->page_title->show();
        $uploadFileData['file_name'] = '';
        try
        {
            $this->breadcrumbs->unshift(2, lang('Notification_member'), 'admin/society/sendNotification');
            $this->data['breadcrumb'] = $this->breadcrumbs->show();
            if(!empty($_POST))
            {
                $userId = $this->session->userdata['user_id'];
                define ("MAX_SIZE","110");
                $this->form_validation->set_rules('notification', 'lang:offers_label', 'required');
                $this->form_validation->set_rules('notification_title', 'lang:Notification_title', 'required');
                $this->form_validation->set_rules('description', 'lang:Notification_description', 'required');
                if($this->form_validation->run() == TRUE)
                {
                    $directoryPath = $this->config->item('image_upload_path');                     
                    $directotyPathArray = array('images','notification_images');
                    foreach ($directotyPathArray as $key => $value) 
                    {
                        $directoryPath = $directoryPath.$value.'/';
                        if (!is_dir($directoryPath)) 
                        {
                            mkdir($directoryPath,0777);
                        }
                    }
                    $this->load->library('Upload_file');

                    if($_FILES['image']['size'] != 0)
                    {
                        $date = date("Y-m-d H:i:s");
                        $notifiName =  md5($date . mt_rand(1, 99999));
                        $notifiFileName = stripslashes($_FILES['image']['name']);
                        $notifiExtension = $this->getExtension($notifiFileName);
                        $notificationImage = $notifiName.'.'.$notifiExtension;
                        $notifiImage=filesize($_FILES['image']['tmp_name']);

                        if ($notifiImage < MAX_SIZE*1024)
                        {
                          set_alert("danger", "You have uploaded small size image");
                          redirect(base_url()."admin/society/memberNotification");

                        }

                        if($notifiExtension=="jpg" || $notifiExtension=="jpeg" )
                        {
                            $uploadedOfferFile = $_FILES['image']['tmp_name'];
                            $notifiSrc = imagecreatefromjpeg($uploadedOfferFile);
                        }
                        else if($notifiExtension=="png")
                        {
                            $uploadedOfferFile = $_FILES['image']['tmp_name'];
                            $notifiSrc = imagecreatefrompng($uploadedOfferFile);
                        }
                        else 
                        {
                            $notifiSrc = imagecreatefromgif($uploadedOfferFile);
                        }

                        list($notifiWidth,$notifiHeight)=getimagesize($uploadedOfferFile);

                        $newNotifiWidth=800;
                        $newNotifiHeight=600;
                        $notifiTmp=imagecreatetruecolor($newNotifiWidth,$newNotifiHeight);

                        imagecopyresampled($notifiTmp,$notifiSrc,0,0,0,0,$newNotifiWidth,$newNotifiHeight,

                        $notifiWidth,$notifiHeight);

                        $notificationFileName = $directoryPath."/". $notificationImage;
                       
                        imagejpeg($notifiTmp,$notificationFileName,100);

                        imagedestroy($notifiSrc);
                        imagedestroy($notifiTmp);
                       
                    }
                        if(!empty($_POST['user_id']))
                        {
                            foreach ($_POST['user_id'] as $ukey => $uvalue)
                            {
                                 $saveData = array(
                                        'user_id' => $uvalue,
                                        'notification_title' => $_POST['notification_title'],
                                        'image'  => $notificationImage,
                                        'description' =>$_POST['description'],
                                        'notification' =>$_POST['notification'],
                                        'url' => $_POST['url'],
                                        'created_by' => $userId,
                                        );
                                $saveNotification = $this->Api_model->create('send_notification',$saveData);
                                if(!empty($saveNotification))
                                {
                                     $userCondition = array('id' => $uvalue);
                                        $userData = $this->Api_model->selectAllData('app_users',$userCondition);
                                       $regid = isset($userData[0]['device_token']) ? $userData[0]['device_token'] : "";
                                       $api_access_key = $this->config->item('api_access_key');
                                        $message = array(
                                            'notification_title' => $_POST['notification_title'],
                                            'image'  => $notificationImage,
                                            'description' =>$_POST['description'],
                                            'url' => $_POST['url'],
                                            'image_url' =>  $this->config->item('uploaded_image_url').'images/notification_images/'.$notificationImage
                                            );

                                        $this->Api_model->push_notification($regid,$api_access_key,$message);
                                }
                                else
                                {   
                                    set_alert("danger", "Unable To Send Notification");
                                    redirect(base_url()."admin/society/memberNotification");
                                }
                            }
                            set_alert("success","Notification sent successfully");
                            redirect(base_url()."admin/dashboard");
                        }
                        else
                        {
                            redirect(base_url()."admin/society/memberNotification");
                        }
                }
            }
            else
            {   
                $this->data['user_id'] = '';
                $this->data['notification_title'] = '';
                $this->data['description'] = '';
                $this->data['notification'] = '';
                $this->data['image'] ='';
                $this->data['url'] ='';
                $this->data['created_by'] ='';
                
                $this->template->admin_render('admin/society/membersfornotification', $this->data);
            }
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
   }

   public function getCityList()
   {
        if(!empty($_POST))
        {
            $cityCondition = array('state_id'=>$_POST['state_id']);
            $cityData = $this->Api_model->selectCity('city_master',$cityCondition);
            $html = '';
            foreach ($cityData as $key => $value) 
            {
                $city = strtolower($value['city_name']);
                $city[0] = strtoupper($city[0]);
                $html .= '<option value="'.$city.'">'.$city.'</option>';

            }
            echo json_encode($html,true); exit;
        }
   }

   function getExtension($str) {

         $i = strrpos($str,".");
         if (!$i) { return ""; } 

         $l = strlen($str) - $i;
         $ext = substr($str,$i+1,$l);
         return $ext;
    }

    public function privacyPolicy()
    {   
        $this->page_title->push(lang('privacy-policy'));
        $this->data['pagetitle'] = $this->page_title->show();
        $this->breadcrumbs->unshift(1, lang('privacy-policy'), 'admin/society');
        try
        {
            
            $this->data['breadcrumb'] = $this->breadcrumbs->show();
            $this->template->admin_render('admin/society/privacy-policy', $this->data);
        }
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }
}