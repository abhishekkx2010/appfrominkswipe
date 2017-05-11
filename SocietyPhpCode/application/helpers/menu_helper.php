<?php
defined('BASEPATH') OR exit('No direct script access allowed');

if ( ! function_exists('active_link_controller'))
{
    function active_link_controller($controller)
    {
        $CI    =& get_instance();
        $class = $CI->router->fetch_class();

        return ($class == $controller) ? 'active' : NULL;
    }
}


if ( ! function_exists('active_link_function'))
{
    function active_link_function($controller)
    {
        $CI    =& get_instance();
        $class = $CI->router->fetch_method();

        return ($class == $controller) ? 'active' : NULL;
    }
}

if ( ! function_exists('active_link_controller_function'))
{
    function active_link_controller_function($controller, $function, $function1=NULL, $function2=NULL, $function3=NULL)
    {
        $CI    =& get_instance();
        $class = $CI->router->fetch_class();
        $method = $CI->router->fetch_method();

        if(!empty($controller) && !empty($function))
        {
            if($class == $controller)
            {
                if(!empty($function3))
                {
                    return ($method == $function || $method == $function1 || $method == $function2 || $method == $function3) ? 'active' : NULL;
                }
                if(!empty($function2))
                {
                    return ($method == $function || $method == $function1 || $method == $function2 || $method == $function3) ? 'active' : NULL;
                }
                if(!empty($function1))
                {
                    return ($method == $function || $method == $function1) ? 'active' : NULL;
                }
                else
                {
                    return ($method == $function) ? 'active' : NULL;
                }
            }
        }
    }
}
