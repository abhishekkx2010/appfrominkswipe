<?php
defined('BASEPATH') OR exit('No direct script access allowed');
$is_create = $this->uri->segment(3) == "createOffer" ? TRUE : FALSE;
if(isset($selected_society))
{
    foreach ($selected_society as $skey => $svalue) 
    {
        $societyId[] = $svalue['society_id'];
    }

} ?>
    <style type="text/css">
        .sorting {
            text-align: center;
        }
        
        input#datepicker.form-control.date_pick.input-sm.valid.hasDatepicker {
            height: 34px !important;
        }
        
        input#datepicker1.form-control.date_pick.input-sm.valid.hasDatepicker {
            height: 34px !important;
        }
        
        .mt20 {
            margin-top: 20px;
        }
        
        .span_error {
            color: #FF0000;
        }
        
        span.span_error p {
            color: #FF0000;
        }
    </style>

    <div class="content-wrapper">
        <input type="hidden" name="logoimage" id="logoimage" value="<?php echo $is_create;?>">
        <input type="hidden" name="offerimage" id="offerimage" value="<?php echo $is_create;?>">

        <section class="content-header">
            <?php echo $pagetitle; ?>
            <?php echo $breadcrumb; ?>
            <div class="clearfix"></div>
        </section>
        <div id="showalert" class="alert alert-danger alert-msg" style="margin-top:10px; display:none;">Please select at least one society.</div>
        <div style="margin-top:10px;">
            <?php echo get_alert();?>
        </div>
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title">
                                <?php echo $is_create ? lang('') : lang(''); ?>
                            </h3>
                        </div>
                        <div class="box-body">
                            <?php echo form_open_multipart(current_url(), array('class' => 'form-horizontal', 'id' => 'form_action_offers')); ?>
                            <!-- <div class="form-group"> -->
                            <div class="col-md-8">
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_label', 'offers_label', array('class' => 'control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-9">
                                        <input type="radio" name="shared_with" value="Public" id="hidelist" <?php if($shared_with=='Public' ){ echo 'checked="checked"';}?> checked="checked"/> All Societies &nbsp;&nbsp; <input type="radio" class="applicableto" name="shared_with" value="Private" id="showlist" <?php if($shared_with=='Private' ){ echo 'checked="checked"';}?>> Select Societies
                                        <span class="span_error"><?php echo form_error('shared_with'); ?></span>
                                    </div>
                                </div>
                                <?php if ($is_create == 'createOffer') {?>
                                <div class="row mt20" id="testdiv" style="display:none">
                                    <?php echo lang('society_name', 'society_name', array('class' => 'col-md-3 control-label-coupon')); ?>
                                    <div class="col-md-9 applicable-scroll p0">
                                        <!-- <div class="table-responsive"> -->
                                        <table id="example1" class="table table-bordered table-striped dataTable ztp_detection_table" role="grid" aria-describedby="example1_info">
                                            <thead>
                                                <tr role="row" class="input_width" id="filterrow">
                                                    <th class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">Select Society<br>

                                                        <th class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">Society Name <br>

                                                        </th>
                                                        <th class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">City <br>

                                                        </th>
                                                        <th class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">Pin Code <br>

                                                        </th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <?php foreach ($society_data as $key => $value) {?>
                                                <tr class="text-center" id="filterrow">
                                                    <td><input type="checkbox" name="society_id[]" id="check_user" data-id="individual" class="check" value="<?php echo $value['id']?>" /></td>
                                                    <td>
                                                        <?php echo $value['name']?>
                                                    </td>
                                                    <td>
                                                        <?php echo $value['city']?>
                                                    </td>
                                                    <td>
                                                        <?php echo $value['pincode']?>
                                                    </td>
                                                </tr>
                                                <?php }?>
                                            </tbody>
                                        </table>
                                        <!-- </div> -->
                                    </div>
                                </div>
                                <?php } else {?>
                                <div class="row mt20" id="testdiv" style="display:none">
                                    <?php echo lang('society_name', 'society_name', array('class' => 'col-md-3 control-label-coupon')); ?>
                                    <div class="col-md-9 applicable-scroll p0">
                                        <!-- <div class="table-responsive"> -->
                                        <table id="example1" class="table table-bordered table-striped dataTable ztp_detection_table" role="grid" aria-describedby="example1_info">
                                            <thead>
                                                <tr role="row" class="input_width">
                                                    <th class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">Select Society<br>

                                                        <th class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">Society Name <br>
                                                            <input class="form-control input-sm mt10" placeholder="" aria-controls="example1" type="search">
                                                        </th>
                                                        <th class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">City <br>
                                                            <input class="form-control input-sm mt10" placeholder="" aria-controls="example1" type="search">
                                                        </th>
                                                        <th class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">Pin Code <br>
                                                            <input class="form-control input-sm mt10" placeholder="" aria-controls="example1" type="search">
                                                        </th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <?php foreach ($society_data as $key => $value) {?>
                                                <tr class="text-center" id="filterrow">
                                                    <td><input type="checkbox" name="society_id[]" data-id="individual" class="check" value="<?php echo $value['id']?>" <?php if(!empty($societyId)) {if(in_array($value[ 'id'],$societyId)){ echo 'checked="checked"';}}?>/></td>
                                                    <td>
                                                        <?php echo $value['name']?>
                                                    </td>
                                                    <td>
                                                        <?php echo $value['city']?>
                                                    </td>
                                                    <td>
                                                        <?php echo $value['pincode']?>
                                                    </td>
                                                </tr>
                                                <?php }?>
                                            </tbody>
                                        </table>
                                        <!-- </div> -->
                                    </div>
                                </div>
                                <?php } ?>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_title', 'offers_title', array('class' => 'control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control modal_text_width" name="title" id="offers_title" value="<?php echo $offersTitle; ?>" placeholder="Title" maxlength="100" /><br>
                                        <span class="span_error"><?php echo form_error('title'); ?></span>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_description', 'offers_description', array('class' => 'control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control modal_text_width" name="description" id="description" value="<?php echo $description; ?>" placeholder="Description" maxlength="500" /><br>
                                        <span class="span_error"><?php echo form_error('description'); ?></span>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_category', 'offers_category', array('class' => ' control-label-coupon')); ?>
                                    </div>
                                    <div class="col-md-9">
                                        <select class="form-control modal_text_width" name="category" id="category">
                                                                <option value="online coupons and discounts" <?php echo $category=="online coupons and discounts"?"selected":"";?>>Online Coupons and Discounts</option>
                                                                <option value="product deals" <?php echo $category=="product deals"?"selected":"";?>>Product Deals</option>
                                                                <option value="clearance" <?php echo $category=="clearance"?"selected":"";?>>Clearance</option>
                                                                <option value="free trial" <?php echo $category=="free trial"?"selected":"";?>>Free Trial</option> 
                                                                <option value="subscriptions" <?php echo $category=="subscriptions"?"selected":"";?>>Subscriptions</option> 
                                                                <option value="discounted gift cards" <?php echo $category=="discounted gift cards"?"selected":"";?>>Discounted Gift Cards</option>            
                                                            </select>
                                        <br>
                                        <span class="span_error"><?php echo form_error('category'); ?></span>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_url', 'offers_url', array('class' => 'control-label-coupon')); ?>
                                    </div>
                                    <div class="col-md-9">
                                        <input type="url" class="form-control modal_text_width" name="url" id="url" value="<?php echo $url; ?>" placeholder="Eg. https://www.google.com" /><br>
                                        <span class="span_error"><?php echo form_error('url'); ?></span>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_company_name', 'offers_company_name', array('class' => 'control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control modal_text_width" name="company_name" id="company_name" value="<?php echo $company_name; ?>" placeholder="Company Name" maxlength="100" /><br>
                                        <span class="span_error"><?php echo form_error('company_name'); ?></span>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_company_address', 'offers_company_address', array('class' => 'control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control modal_text_width" name="company_address" id="company_add" value="<?php echo $company_address ?>" placeholder="Company Address" maxlength="100" /><br>
                                        <span class="span_error"><?php echo form_error('company_address'); ?></span>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_logo', 'offers_logo', array('class' => 'control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-9 mt5">
                                        <input type="file" name="offer_logo" id="offer_logo" accept=".jpg,.jpeg,.png,.bmp">
                                        <span>( Upload image of Size 200px * 200px )</span>
                                        <br>
                                        <span class="span_error"><?php echo form_error('offer_logo'); ?></span>
                                        <?php if(!empty($offer_logo)) {?>
                                        <img src="<?php echo $this->config->item('uploaded_image_url').'images/offer_images/'.$offer_logo;?>" style="width:104px;height:78px;" class="img-responsive">
                                        <?php }?>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_image', 'offers_image', array('class' => 'control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-9 mt5">
                                        <input type="file" name="offer_image" id="offer_image" accept=".jpg,.jpeg,.png,.bmp">
                                        <span>( Upload image of Size 800px * 600px )</span>
                                        <br>
                                        <span class="span_error"><?php echo form_error('offer_image'); ?></span>
                                        <?php if(!empty($offer_image)) {?>
                                        <img src="<?php echo $this->config->item('uploaded_image_url').'images/offer_images/'.$offer_image;?>" style="width:104px;height:78px;" class="img-responsive">
                                        <?php } ?>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_contact_person', 'offers_contact_person', array('class' => 'control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control modal_text_width" name="contact_person_name" id="contact_person" value="<?php echo $contact_person_name;?>" placeholder="Contact Name" maxlength="100" /><br>
                                        <span class="span_error"><?php echo form_error('contact_person_name'); ?></span>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_contact_person_mobile', 'offers_contact_person_mobile', array('class' => 'control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-9">
                                        <input type="tel" class="form-control modal_text_width" name="contact_person_mobile" id="contact_mobile" value="<?php echo $contact_person_mobile; ?>" placeholder="Contact Person Mobile number" maxlength="10" /><br>
                                        <span class="span_error"><?php echo form_error('contact_person_mobile'); ?></span>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_contact_person_email', 'offers_contact_person_email', array('class' => 'control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-9">
                                        <input type="email" class="form-control modal_text_width" name="contact_person_email" id="contact_email" value="<?php echo $contact_person_email; ?>" placeholder="Contact Person Email Id" /><br>
                                        <span class="span_error"><?php echo form_error('contact_person_email'); ?></span>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_amount', 'offers_amount', array('class' => 'control-label-coupon')); ?>
                                    </div>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control modal_text_width" name="amount" value="<?php echo $amount; ?>" placeholder="Amount" maxlength="50"><br>
                                        <span class="span_error"><?php echo form_error('amount'); ?></span>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_creator_notes', 'offers_creator_notes', array('class' => 'control-label-coupon')); ?>
                                    </div>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control modal_text_width" name="creator_notes" id="creator_notes" value="<?php echo $creator_notes; ?>" placeholder="Creator Notes" maxlength="100" /><br>
                                        <span class="span_error"><?php echo form_error('creator_notes'); ?></span>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('common_status', 'common_status', array('class' => 'control-label-coupon')); ?>
                                    </div>
                                    <div class="col-md-9 mt5">
                                        <select class="form-control modal_text_width" name="status" id="status">
                                                                        <option value="1" <?php echo $status=="1"?"selected":""; ?>>Active</option>
                                                                        <option value="0" <?php echo $status=="0"?"selected":""; ?>>Inactive</option>
                                                                    </select>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_start_date', 'offers_start_date', array('class' => ' control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-9">
                                        <div class="input-group no-overflow" data-date-format="dd-mm-yyyy">
                                            <input name="start_date" class="form-control date_pick input-sm valid" id="datepicker" placeholder="Start Date" value="<?php echo $start_date; ?>" aria-required="true" aria-invalid="false" type="text">
                                            <span class="input-group-addon datepicker"><i class="fa fa-calendar" aria-hidden="true" id="datepicker"></i></span>
                                        </div><br>
                                        <span class="span_error"><?php echo form_error('start_date'); ?></span>
                                    </div>
                                </div>
                                <div class="row mt20">
                                    <div class="col-md-3">
                                        <?php echo lang('offers_expiry', 'offers_expiry', array('class' => ' control-label-coupon')); ?>
                                        <span class="span_error">*</span>
                                    </div>
                                    <div class="col-md-9">
                                        <div class="input-group no-overflow" data-date-format="dd-mm-yyyy">
                                            <input name="expire_on" class="form-control date_pick input-sm valid" id="datepicker1" placeholder="Expiry Date" value="<?php echo $expire_on; ?>" aria-required="true" aria-invalid="false" type="text">
                                            <span class="input-group-addon datepicker1"><i class="fa fa-calendar" aria-hidden="true" id="datepicker1"></i></span>
                                        </div><br>
                                        <span class="span_error"><?php echo form_error('expire_on'); ?></span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="box-footer">
                            <?php $action = $is_create ? "Add" : "Update"; ?>
                            <div class="col-md-12 col-sm-12 mt6">
                                <div class="btn-group createoffer_btn pull-right">
                                    <?php echo form_button(array('type' => 'submit', 'class' => 'btn btn-success btn-flat color_add', 'content' => $action)); ?>
                                    <?php echo anchor('admin/society/offersview', lang('actions_cancel'), array('class' => 'btn btn-default btn-flat color_close')); ?>
                                </div>
                            </div>
                        </div>
                        <?php echo form_close();?>
                    </div>
                </div>
        </section>
        </div>
        <?php if($shared_with == 'Private'){?>
        <script>
            $(document).ready(function() {
                $('#testdiv').show();
            });
        </script>
        <?php }?>


        <script type="text/javascript">
            $(document).ready(function() {

                $('.applicableto').click(function() {
                    var applicableto = $(this).val();
                    var cells = table.cells().nodes();
                    var allVals = [];
                    if (applicableto == 'Public') {
                        $(cells).find(':checkbox').prop('checked', $(this).is(':checked'));
                    } else {
                        $(cells).find(':checkbox').prop('checked', '');
                    }
                });

                $('#example1 thead tr#filterrow th').each(function() {
                    var title = $('#example1 thead th').eq($(this).index()).text();
                    var select_array = new Array("Select Society");

                    if (select_array.indexOf(title) == "-1") {
                        $(this).html(title + '<input type="text" class="form-control input-sm"/>');
                    } else {
                        $(this).html(title);
                    }
                });

                $("#example1 thead input").on('keyup change', function() {
                    table
                        .column($(this).parent().index() + ':visible')
                        .search(this.value)
                        .draw();
                });

                var table = $('#example1').DataTable({
                    orderCellsTop: true,
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": false,
                    "info": true,
                    "autoWidth": true
                });

                $("#example1_paginate").addClass("pull-right");
                $("#example1_filter").remove();

                var logoimage = $('#logoimage').val();
                var offerimage = $('#offerimage').val();

                if (logoimage == 1) {
                    var logorequire = true;
                } else {
                    var logorequire = false;
                }

                if (offerimage == 1) {
                    var offerrequire = true;
                } else {
                    var offerrequire = false;
                }

                $.validator.addMethod("alpha", function(value, element) {
                    return this.optional(element) || value == value.match(/^[a-zA-Z\s]+$/);
                }, "Please enter only Alphabets.");
                $.validator.addMethod("number1", function(value, element) {
                    return this.optional(element) || value == value.match(/^[789][0-9]{9}$/);
                }, "Please enter 10 digit valid mobile Number.");
                $.validator.addMethod("weburl", function(value, element) {
                    return this.optional(element) || value == value.match(/\b(?:(?:https?|ftp):\/\/|www\.)[-a-z0-9+&@#\/%?=~_|!:,.;]*[-a-z0-9+&@#\/%=~_|]/i);
                }, "Please enter valid url.");

                var IsImageRequired = "true";
                $("#form_action_offers").validate({
                    rules: {
                        shared_with: {
                            required: true,
                        },
                        title: {
                            required: true,
                            maxlength: 100
                        },
                        description: {
                            required: true,
                            maxlength: 500
                        },
                        created_by: {
                            required: true
                        },
                        created_on: {
                            required: true
                        },
                        expire_on: {
                            required: true
                        },
                        start_date: {
                            required: true
                        },
                        company_name: {
                            required: true,
                            maxlength: 100
                        },
                        company_address: {
                            required: true,
                            maxlength: 100
                        },
                        contact_person_name: {
                            required: true,
                            maxlength: 100,
                            alpha: true,
                        },
                        contact_person_mobile: {
                            required: true,
                            number1: true,
                            maxlength: 10
                        },
                        contact_person_email: {
                            required: true
                        },
                        offer_logo: {
                            required: logorequire
                        },
                        offer_image: {
                            required: offerrequire
                        },
                        amount: {
                            number: true
                        },
                        url: {
                            weburl: true
                        },
                    },
                    messages: {
                        shared_with: {
                            required: "Applicable to is required!"
                        },
                        title: {
                            required: "Offer title is required!",
                            maxlength: "Offer title is not more than 100 characters!"
                        },
                        description: {
                            required: "Offer Description is required!",
                            maxlength: "Description is not more than 500 characters!"
                        },
                        created_by: {
                            required: " created_by is required!"
                        },
                        expire_on: {
                            required: " Offer expiry date is required!"
                        },
                        start_date: {
                            required: "Start date is required!"
                        },
                        company_name: {
                            required: "Company name is required!",
                            maxlength: "Company name is not more than 100 characters!"
                        },
                        company_address: {
                            required: "Company address is required!",
                            maxlength: "Company address is not more than 100 characters!"
                        },
                        contact_person_name: {
                            required: "Contact person name is required!",
                            maxlength: "Contact person name is not more than 100 characters!"
                        },
                        contact_person_mobile: {
                            required: "Contact number is required!"
                        },
                        contact_person_email: {
                            required: "Contact person email id is required!"
                        },
                        offer_logo: {
                            required: "Logo image is required!"
                        },
                        offer_image: {
                            required: "Offer image is required!"
                        },
                        amount: {
                            required: "Please enter number!"
                        },
                        amount: {
                            required: "Please enter number!"
                        },

                    },
                    errorPlacement: function(error, element) {
                        var my = "";
                        var at = "";

                        if ($(window).width() > 800) {
                            my = 'bottom right';
                            at = 'top right';
                        } else {
                            my = 'top right';
                            at = 'bottom right';
                        }
                        if (!error.is(':empty')) {
                            $(element).not('.valid').qtip({
                                    overwrite: false,
                                    content: error,
                                    hide: false,
                                    show: {
                                        event: false,
                                        ready: true
                                    },
                                    position: {
                                        my: my,
                                        at: at,
                                        viewport: $(window),
                                        adjust: {
                                            x: 7,
                                            y: -2
                                        }
                                    },
                                    style: {
                                        classes: 'qtip-red qtip-rounded qtip-shadow',
                                        tip: {
                                            height: 6,
                                            width: 11
                                        }
                                    }
                                })
                                .qtip('option', 'content.text', error);
                        } else {
                            element.qtip('destroy');
                        }
                    },
                    submitHandler: function(form) {
                        var cells = table.cells().nodes();
                        var selectedIds = [];
                        $(cells).find(':checked').each(function() {
                            selectedIds.push($(this).val());
                        });
                        var checked = $('#check_user:checkbox:checked').length;
                        if (document.getElementById('showlist').checked) {
                            if (selectedIds == '') {
                                $('#showalert').show();
                                window.scrollTo(500, 0);
                                return false;
                            } else {
                                form.submit();
                                $('#showalert').hide();
                            }
                        }
                        form.submit();
                        $('#showalert').hide();
                    },
                    success: "valid",
                });

                var dateToday = new Date();
                $("#datepicker").datepicker({
                    minDate: dateToday,
                    dateFormat: 'yy-mm-dd',
                    onSelect: function(selected) {
                        inline: true;
                        format: 'yyyy-mm-dd';
                        var dt = new Date(selected);
                        dt.setDate(dt.getDate() + 1);
                        $("#datepicker1").datepicker("option", "minDate", dt);
                    }
                });

                $("#datepicker1").datepicker({
                    minDate: dateToday,
                    dateFormat: 'yy-mm-dd',
                    onSelect: function(selected) {
                        inline: true;
                        dateFormat: 'yyyy-mm-dd';
                        var dt = new Date(selected);
                        dt.setDate(dt.getDate() - 1);
                        $("#datepicker").datepicker("option", "maxDate", dt);
                    }
                });

                $('#showlist').click(function() {
                    $('#testdiv').show();
                });
                
                $('#hidelist').click(function() {
                    $('#testdiv').hide();
                });

                var _URL = window.URL || window.webkitURL;

                $("#offer_image").on('change', function(e) {
                    var image, file;
                    if ((file = this.files[0])) {
                        image = new Image();
                        var input = $('#offer_image');
                        image.onload = function() {
                            var height = image.height;
                            var width = image.width;
                            var ratio = width / height;
                            var size = file.size;

                            if (width != 800 || height != 600) {

                                swal("Image size should be 800px*600px");
                                input.replaceWith(input.val('').clone(true));
                                return false;
                            }
                        };
                        image.src = _URL.createObjectURL(file);
                    }
                });

                $("#offer_logo").on('change', function(e) {
                    var image, file;
                    if ((file = this.files[0])) {
                        image = new Image();
                        var input = $('#offer_logo');
                        image.onload = function() {
                            var height = image.height;
                            var width = image.width;
                            var ratio = width / height;
                            var size = file.size;
                            if (width != 200 || height != 200) {
                                swal("Image size should be 200px*200px");
                                input.replaceWith(input.val('').clone(true));
                                return false;
                            }
                        };
                        image.src = _URL.createObjectURL(file);
                    }
                });
            });
        </script>

        <script type="text/javascript">
            $(document).ready(function() {

                $("#selecct_all").change(function() {
                    $(".check").prop('checked', $(this).prop("checked"));
                });

                $('.check').change(function() {
                    if (false == $(this).prop("checked")) {
                        $("#selecct_all").prop('checked', $(this).prop("checked"));
                    }
                });

                var $checkboxes = $('.check');

                $checkboxes.change(function() {
                    var type = $(this).data("id");
                    var coun_checkboxes = $checkboxes.filter(':checked').length;
                    if (type == 'all') {
                        if (coun_checkboxes != '' && coun_checkboxes != null && coun_checkboxes != undefined && coun_checkboxes > 0) {
                            var count = parseFloat(coun_checkboxes) - parseFloat(1);
                        } else {
                            var count = parseFloat(coun_checkboxes);
                        }
                    } else {
                        var count = parseFloat(coun_checkboxes);
                    }

                    $('#count-checked-checkboxes').text(count);

                });



            });
        </script>