<?php

	function set_alert($type, $message)
	{
		$html = '<div class="col-md-12 alert_div"><div class="alert alert-'.$type.' alert_msg">'.$message.'</div></div>';

		$CI =& get_instance();
		$CI->session->set_flashdata('message', $html);
		return true;
	}

	function get_alert()
	{
		$CI =& get_instance();
		return $CI->session->flashdata('message');
	}

?>