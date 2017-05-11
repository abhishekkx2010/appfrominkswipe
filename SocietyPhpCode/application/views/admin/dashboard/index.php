<?php
defined('BASEPATH') OR exit('No direct script access allowed');

?>

            <div class="content-wrapper">
                <section class="content-header">
                    <?php echo $pagetitle; ?>
                    <?php echo $breadcrumb; ?>
                </section>

                <section class="content">
                   
                    <div class="row">
                        <div class="col-md-12">
                            <div class="box box-success">
                                <div class="box-header with-border">
                                    <h2 class="box-title"></h2>
                                </div>
                                <div class="box-body">
                                    <div class="col-md-3 col-sm-6 col-xs-12">
                            <div class="info-box">
                            <a href="<?php echo base_url().'admin/society/index/'?>">
                                <span class="info-box-icon bg-green"><i class="fa fa-building-o"></i></span>
                                <div class="info-box-content">
                                
                                    <span class="info-box-text">Total Societies</span>
                                    <span class="info-box-number"><?php echo $count_society; ?></span>
                                </a>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-6 col-xs-12">
                            <div class="info-box">
                                <a href="<?php echo base_url().'admin/society/index/'?>">
                                    <span class="info-box-icon bg-green"><i class="fa fa-home"></i></span>
                                    <div class="info-box-content">
                                    
                                        <span class="info-box-text">Total Properties</span>
                                        <span class="info-box-number"><?php echo $count_properties; ?></span>
                                </a>
                                </div>
                            </div>
                        </div>

                        <div class="clearfix visible-sm-block"></div>

                        <div class="col-md-3 col-sm-6 col-xs-12">
                            <div class="info-box">
                                <a href="<?php echo base_url().'admin/society/appMembers/'?>">
                                    <span class="info-box-icon bg-green"><i class="fa fa-users"></i></span>
                                    <div class="info-box-content">
                                    
                                        <span class="info-box-text">Total Members</span>
                                        <span class="info-box-number"><?php echo $count_users; ?></span>
                                </a>
                            </div>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-6 col-xs-12">
                            <div class="info-box">
                                <a href="<?php echo base_url().'admin/society/offersview/'?>">
                                    <span class="info-box-icon bg-green"><i class="fa fa-shopping-bag"></i></span>
                                    <div class="info-box-content">
                                        <span class="info-box-text">Offers</span>
                                        <span class="info-box-number"><?php echo $count_offers; ?></span>
                                </a>
                                </div>
                            </div>
                        </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
