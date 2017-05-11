<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/*
| -------------------------------------------------------------------------
| URI ROUTING
| -------------------------------------------------------------------------
| This file lets you re-map URI requests to specific controller functions.
|
| Typically there is a one-to-one relationship between a URL string
| and its corresponding controller class/method. The segments in a
| URL normally follow this pattern:
|
|	example.com/class/method/id/
|
| In some instances, however, you may want to remap this relationship
| so that a different class/function is called than the one
| corresponding to the URL.
|
| Please see the user guide for complete details:
|
|	http://codeigniter.com/user_guide/general/routing.html
|
| -------------------------------------------------------------------------
| RESERVED ROUTES
| -------------------------------------------------------------------------
|
| There are three reserved routes:
|
|	$route['default_controller'] = 'welcome';
|
| This route indicates which controller class should be loaded if the
| URI contains no data. In the above example, the "welcome" class
| would be loaded.
|
|	$route['404_override'] = 'errors/page_missing';
|
| This route will tell the Router which controller/method to use if those
| provided in the URL cannot be matched to a valid route.
|
|	$route['translate_uri_dashes'] = FALSE;
|
| This is not exactly a route, but allows you to automatically route
| controller and method names that contain dashes. '-' isn't a valid
| class or method name character, so it requires translation.
| When you set this option to TRUE, it will replace ALL dashes in the
| controller and method URI segments.
|
| Examples:	my-controller/index	-> my_controller/index
|		my-controller/my-method	-> my_controller/my_method
*/
$route['default_controller'] = 'auth';
$route['404_override'] = '';
$route['translate_uri_dashes'] = FALSE;

$route['admin']						    = 'admin/dashboard';
$route['admin/prefs/interfaces/(:any)'] = 'admin/prefs/interfaces/$1';
$route['get-city-list'] 				= "ApiController/getCityList";
$route['get-state-list'] 				= "ApiController/getStateList";
$route['get-country-list'] 			    = "ApiController/getCountryList";
$route['send-otp'] 						= "ApiController/sendOtpToUser";
$route['verify-otp'] 					= "ApiController/verifyOtp";
$route['test-otp'] 						= "ApiController/testOtp";
$route['register-app-user'] 			= "ApiController/register";
$route['login-app-user']				= "ApiController/login";
$route['logout-app-user']				= "ApiController/logout";
$route['edit-app-user']                 = "ApiController/editProfile";
$route['forgot-password']			    = "ApiController/forgotPassword";
$route['reset-password']			    = "ApiController/resetPassword";
$route['view-user-profile']			    = "ApiController/viewProfile";
$route['register-society']			    = "ApiController/registerSociety";
$route['search-society'] 				= "ApiController/searchSociety";
$route['register-complaint']			= "ApiController/registerComplaint";
$route['register-announcements']		= "ApiController/registerAnnouncements";
$route['get-property-type']    			= "ApiController/getpropertyType";
$route['register-property']    			= "ApiController/registerProperty";
$route['delete-property']				= "ApiController/deleteProperty";
$route['get-user-announcements']   		= "ApiController/getUserAnnouncements";
$route['get-user-property'] 			= "ApiController/getUserProperty";
$route['get-society-announcements']		= "ApiController/getSocietyAnnouncements";
$route['edit-user-property'] 			= "ApiController/editUserProperty";
$route['get-user-society'] 				= "ApiController/getMySociety";
$route['add-user-post']					= "ApiController/addUserPost";
$route['get-society-post']				= "ApiController/getPostBySociety";
$route['get-user-post']				    = "ApiController/getPostByUser";
$route['get-user-society'] 			    = "ApiController/getMySociety";
$route['get-society-member']            = "ApiController/getSocietyMember";
$route['add-user-comment']				= "ApiController/addPostComment";
$route['add-user-poll']					= "ApiController/addUserPoll";
$route['get-user-poll']					= "ApiController/getUserPoll";
$route['publish-poll-result']			= "ApiController/publishPollResult";
$route['get-arhive-poll']				= "ApiController/getArchivePoll";
$route['edit-end-date']					= "ApiController/editEndDate";
$route['archive-poll']					= "ApiController/archivePoll";
$route['delete-poll']					= "ApiController/deletePoll";
$route['get-offers']					= "ApiController/getOffersDetails";
$route['add-poll-option']				= "ApiController/addPollOption";
$route['get-poll-detail']				= "ApiController/getPollDetails";
$route['get-poll-detail']				= "ApiController/getPollDetails";
$route['add-user-group']				= "ApiController/addSocietyUserGroups";
$route['get-user-group']				= "ApiController/getSocietyUserGroups";
$route['get-group-members']				= "ApiController/getSocietyGroupMember";
$route['delete-group']					= "ApiController/deleteSocietyGroup";
$route['create-society-event']			= "ApiController/createSocietyEvent";
$route['get-society-event']				= "ApiController/getSocietyEvent";
$route['get-archive-event']				= "ApiController/getArchiveEvent";
$route['delete-event']					= "ApiController/deleteEvent";
$route['submit-event']					= "ApiController/submitEvent";
$route['get-event-detail']				= "ApiController/getEventDetail";
$route['get-event-response']			= "ApiController/getEventResponse";
$route['get-user-notification']			= "ApiController/getUserNotification";
$route['user-society-post']			    = "ApiController/getUserSocietyPost";


