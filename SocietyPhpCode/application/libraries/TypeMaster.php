<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class TypeMaster
{
	public $id;
	public $property_type;
	

	public function mapCkisColumns($arrayData)
	{
		$this->id = isset($arrayData['id']) ? $arrayData['id'] : '';
		$this->property_type = isset($arrayData['type_name']) ? $arrayData['type_name'] : '';
		return $this;
	}		
}