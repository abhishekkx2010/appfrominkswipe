<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Dashboard extends Admin_Controller {

    public function __construct()
    {
        parent::__construct();

        /* Load :: Common */
        $this->load->helper('number');
        $this->load->model('admin/dashboard_model');
        $this->load->model('admin/Api_model');
    }


	public function index()
	{
        if ( ! $this->ion_auth->logged_in() OR ! $this->ion_auth->is_admin())
        {
            redirect('auth/login', 'refresh');
        }
        else
        {
            /* Title Page */
            $this->page_title->push(lang('menu_dashboard'));
            $this->data['pagetitle'] = $this->page_title->show();

            /* Breadcrumbs */
            $this->data['breadcrumb'] = $this->breadcrumbs->show();

            /* Data */
            $userCondition = 'status IN(0,1)';
            $this->data['count_users']       = count($this->Api_model->selectAllData('app_users',$userCondition));

            $Societyondition = 'status IN(0,1)';
            $this->data['count_society']       = count($this->Api_model->selectAllData('society',$Societyondition));

            $propertyCondition = 'status IN(0,1)';
            $this->data['count_properties']       = count($this->Api_model->selectAllData('user_properties',$propertyCondition));

            $offersCondition = 'expire_on > Now()' ;
            $this->data['count_offers']      = count($this->Api_model->selectAllData('offers_master',$offersCondition));

            /* Load Template */
            $this->template->admin_render('admin/dashboard/index', $this->data);
        }
	}
}
