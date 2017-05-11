<?php
defined('BASEPATH') OR exit('No direct script access allowed');


    function cityList($state_id,$country_id,$limit,$offset)
    {
    	$CI =& get_instance();
     	$CI->load->library('CityMaster');
        $condition = '(state_id = '.$state_id.' AND country_id = '.$country_id.') ORDER BY city_name ASC';
        $cityData = $CI->api_model->select('city_master', $condition, $limit, $offset);

        foreach ($cityData as $key => $value)
        {
        	$cityMapData[] = (new CityMaster())->mapCkisColumns($value);
        }
        return $cityMapData;
    }
 
    function stateList($country_id,$limit,$offset)
    {
    	$CI =& get_instance();
        $CI->load->library('StateMaster');
        $condition = '(country_id =  '.$country_id.') ORDER BY state_name ASC';
        $stateData = $CI->api_model->select('state_master', $condition, $limit, $offset);
        foreach ($stateData as $key => $value)
        {
           
        	$stateMapData[] = (new StateMaster())->mapCkisColumns($value);
        }
        return $stateMapData;
    }

    function countryList($limit,$offset)
    {
    	$CI =& get_instance();
        $CI->load->library('CountryMaster');
        $condition = 'country_id IS NOT NULL';
        $countryData = $CI->api_model->select('country_master', $condition, $limit, $offset);

        foreach ($countryData as $key => $value)
        {
        	$countryMapData[] = (new CountryMaster())->mapCkisColumns($value);
        }
        return $countryMapData;
    }

    

