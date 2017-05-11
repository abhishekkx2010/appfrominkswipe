<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class StateMaster
{
	public $state_id;
	public $state_name;
	public $country_id;

	public function mapCkisColumns($arrayData)
	{
		$stateName = ucwords(strtolower($arrayData['state_name']));
		$this->state_id = isset($arrayData['state_id']) ? $arrayData['state_id'] : '';
		$this->state_name = isset($stateName) ? $stateName : '';
		$this->country_id = isset($arrayData['country_id']) ? $arrayData['country_id'] : '';
		return $this;
	}		
}