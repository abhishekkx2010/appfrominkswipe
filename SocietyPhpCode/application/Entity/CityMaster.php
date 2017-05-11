<?php


class CityMaster
{
	public $CityId;
	public $CityName;
	public $StateId;
	public $CountryId;

	public function __construct(){
		$this->CityData = array();
	}

	public function mapCkisColumns($arrayData)
	{
		$this->CityId = isset($arrayData['city_id']) ? $arrayData['city_id'] : '';
		$this->CityName = isset($arrayData['city_name']) ? $arrayData['city_name'] : '';
		$this->StateId = isset($arrayData['state_id']) ? $arrayData['state_id'] : '';
		$this->CountryId = isset($arrayData['country_id']) ? $arrayData['country_id'] : '';
		
		return $this;
	}	

	
	
}