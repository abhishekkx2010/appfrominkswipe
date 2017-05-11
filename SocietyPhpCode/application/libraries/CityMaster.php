<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class CityMaster
{
	public $city_id;
	public $city_name;
	public $state_id;
	public $country_id;

	public function mapCkisColumns($arrayData)
	{
		$cityName = ucwords(strtolower($arrayData['city_name']));
		$this->city_id = isset($arrayData['city_id']) ? $arrayData['city_id'] : '';
		$this->city_name = isset($cityName) ? $cityName : '';
		$this->state_id = isset($arrayData['state_id']) ? $arrayData['state_id'] : '';
		$this->country_id = isset($arrayData['country_id']) ? $arrayData['country_id'] : '';
		return $this;
	}		
}