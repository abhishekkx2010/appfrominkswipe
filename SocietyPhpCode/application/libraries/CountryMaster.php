<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class CountryMaster
{
	public $country_id;
	public $country_name;

	public function mapCkisColumns($arrayData)
	{
		$this->country_id = isset($arrayData['country_id']) ? $arrayData['country_id'] : '';
		$this->country_name = isset($arrayData['country_name']) ? $arrayData['country_name'] : '';
		return $this;
	}		
}