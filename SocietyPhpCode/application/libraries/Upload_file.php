<?php
class Upload_file 
{

    function __construct()
    {
        $this->ci =& get_instance();
        $this->ci->load->library('upload');
    }

    function upload_pdf($name_of_file, $upload_path) 
    {
        try 
        {
            $config['upload_path']      = $upload_path;
            $config['allowed_types']    = 'pdf';
            $config['max_size']         = '2000';
            $config['remove_spaces']    = true;
            $config['overwrite']        = false;
            $config['encrypt_name']     = true;
            $config['max_width']        = '';
            $config['max_height']       = '';
                     
            $this->ci->upload->initialize($config);

            if (!$this->ci->upload->do_upload($name_of_file))
            {
                return $this->ci->upload->display_errors();
            }
            else
            {
                return('1');
            }
        } 
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    function upload_image($name_of_image_file, $upload_image_path) 
    {
        try 
        {
            $config['upload_path']      = $upload_image_path;
            $config['allowed_types']    = 'jpg|jpeg|png';
            $config['max_size']         = '2000';
            $config['remove_spaces']    = true;
            $config['overwrite']        = false;
            $config['encrypt_name']     = true;
            $config['max_width']        = '';
            $config['max_height']       = '';
                     
            $this->ci->upload->initialize($config);

            if (!$this->ci->upload->do_upload($name_of_image_file))
            {
                return $this->ci->upload->display_errors();
            }
            else
            {
                return('1');
            }
        } 
        catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    function upload_video($name_of_video_file, $upload_video_path) 
    {
        try 
        {
            $config['upload_path']      = $upload_video_path;
            $config['allowed_types']    = '*';
            $config['max_size']         = '10000000';

            $this->ci->upload->initialize($config);

            if (!$this->ci->upload->do_upload($name_of_video_file))
            {
                return $this->ci->upload->display_errors();
            }
            else
            {
                return('1');
            }
        } 
       catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }

    function youtube_upload($name_of_video) 
    {
        try 
        {
            $url = YOUTUBE_UPLOAD_URL.$name_of_video;

            $ch = curl_init($url);
            curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
            curl_setopt($ch, CURLOPT_HEADER, 0);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
            $response = curl_exec($ch);
            $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
            curl_close($ch);
            if ($status != 200) 
            {
                throw new Exception("Error occour while uploading video", 1);
            }
            else 
            {
                $result['youtube_url'] = json_decode($response);
                if(!empty($result['youtube_url']->id))
                {
                    return $result['youtube_url']->id;
                }
                else
                {
                    throw new Exception("Video is not uploaded", 1);
                }
            }
        } 
       catch(Exception $e)
        {
            $this->data['error'] = $e->getMessage();
            $this->template->admin_render('admin/error', $this->data);
        }
    }
} ?>
