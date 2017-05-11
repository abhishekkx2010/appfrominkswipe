<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?>
<style>
    h4.user_details{ font-size: 16px; }
    .btn.btn-approve{border-color:#008d4c;background-color:#00a65a;}
    .btn.btn-approve:hover{background-color: #008d4c;}
    .btn.btn-reject:hover{background-color:#d6362a;}
    .mb0{ margin-bottom: 0;}
    .btn { background-color: #f44336; color: #ffffff; display: inline-block; font-size: 16px;
        margin-left: 20px; opacity: 0.9; padding: 8px 25px; text-align: center; text-decoration: none;
    }
</style>
<div class="content-wrapper">
    <div class="content-header">
        <?php echo $pagetitle; ?>
        <?php echo $breadcrumb; ?>
        <div class="clearfix"></div>
    </div>
    <div style="margin-top:10px;"><?php echo get_alert();?></div>    
    <div class="content">
        <div class="row">  
            <div class="col-md-12">
                <div class="box box-success">
                   <div class="box-header with-border">
                    <h2 class="box-title"><b>User Id : <span><?php if(isset($societyData[0]['user_id'])){echo $societyData[0]['user_id'];}?></span></b></h2>
                </div>
                <div class="box-body">
                    <div class="col-md-12">                         
                       <!-- New Static Open -->
                       <h4 class="user_details">User Details:</h4>
                       <div class="row">
                           <div class="col-md-6">
                            <dl class="dl-horizontal mb0">
                                <div class="form-group box-header">
                                    <dt>User Name:</dt>
                                    <dd><?php echo $societyData[0]['name']?></dd>
                                </div>
                            </dl>
                        </div>
                        <div class="col-md-6">
                            <dl class="dl-horizontal mb0">
                                <div class="form-group box-header">
                                    <dt>User Email:</dt>
                                    <dd><?php echo $societyData[0]['email']?></dd>
                                </div>
                            </dl>
                        </div>
                    </div>
                    <div class="row">
                       <div class="col-md-6">
                        <dl class="dl-horizontal mb0">
                            <div class="form-group box-header">
                                <dt>Gender:</dt>
                                <dd><?php echo $societyData[0]['gender']?></dd>
                            </div>
                        </dl>
                    </div>
                    <div class="col-md-6">
                        <dl class="dl-horizontal mb0">
                            <div class="form-group box-header">
                                <dt>User Address:</dt>
                                <dd><?php echo $societyData[0]['address']?></dd>
                            </div>
                        </dl>
                    </div>
                </div>
                <div class="row">
                   <div class="col-md-6">
                    <dl class="dl-horizontal mb0">
                        <div class="form-group box-header">
                            <dt>User State:</dt>
                            <dd><?php echo $societyData[0]['state']?></dd>
                        </div>
                    </dl>
                </div>
                <div class="col-md-6">
                    <dl class="dl-horizontal mb0">
                        <div class="form-group box-header">
                            <dt>User City:</dt>
                            <dd><?php echo $societyData[0]['city']?></dd>
                        </div>
                    </dl>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="box box-success">
    <div class="box-header with-border">
     <h3 class="box-title"><b>Society ID : <span><?php if(isset($societyData[0]['society_id'])){echo $societyData[0]['society_id'];}?></span></b></h3>
 </div>
 <div class="box-body">
    <div class="col-md-12">
       <h4 class="user_details">Society Details:</h4>
       <div class="row">
           <div class="col-md-6">
            <dl class="dl-horizontal mb0">
                <div class="form-group box-header">
                    <dt>Society Name:</dt>
                    <dd><?php echo $societyData[0]['society_name']?></dd>
                </div>
            </dl>
        </div>
        <div class="col-md-6">
            <dl class="dl-horizontal mb0">
                <div class="form-group box-header">
                    <dt>Society Address:</dt>
                    <dd><?php echo $societyData[0]['society_address']?></dd>
                </div>
            </dl>
        </div>
    </div>
    <div class="row">
       <div class="col-md-6">
        <dl class="dl-horizontal mb0">
            <div class="form-group box-header">
                <dt>Society State:</dt>
                <dd><?php echo $societyData[0]['society_state']?></dd>
            </div>
        </dl>
    </div>
    <div class="col-md-6">
        <dl class="dl-horizontal mb0">
            <div class="form-group box-header">
                <dt>Society City:</dt>
                <dd><?php echo $societyData[0]['society_city']?></dd>
            </div>
        </dl>
    </div>
</div>
<div class="row">
   <div class="col-md-6">
    <dl class="dl-horizontal mb0">
        <div class="form-group box-header">
            <dt>Society Area:</dt>
            <dd><?php echo $societyData[0]['society_area']?></dd>
        </div>
    </dl>
</div>
<div class="col-md-6">
    <dl class="dl-horizontal mb0">
        <div class="form-group box-header">
            <dt>Society Landmark:</dt>
            <dd><?php echo $societyData[0]['society_landmark']?></dd>
        </div>
    </dl>
</div>
</div>
<div class="row">
    <div class="col-md-6">
        <dl class="dl-horizontal mb0">
            <div class="form-group box-header">
                <dt>Society Pincode:</dt>
                <dd><?php echo $societyData[0]['society_pincode']?></dd>
            </div>
        </dl>
    </div>
    <div class="col-md-6">
        <dl class="dl-horizontal mb0">
            <div class="form-group box-header">
                <dt>Post in Society:</dt>
                <dd><?php if($societyData[0]['post_name'] != '') {echo $societyData[0]['post_name'];} else{echo 'NA';}?></dd>
            </div>
        </dl>
    </div>
</div>
</div>
</div>
</div>

<div class="box box-success">
    <div class="box-body">
        <div class="col-md-12">
            <?php if(!empty($societyData[0]['society_image'])) {?>
            <h4 class="user_details"> Society Image:</h4>
            <div class="row">
               <div class="col-md-6">
                <dl class="dl-horizontal mb0">
                    <div class="form-group">
                        <dt></dt>
                        <dd><img src="<?php echo $this->config->item('uploaded_image_url').'images/society_images/'.$societyData[0]['society_image'];?>" style="width:304px;height:228px;" class="img-responsive"></dd>
                    </div>
                </dl>
            </div>
        </div>
        <?php } ?>  
        <!-- New Static Close -->
        <div class="box-footer">
            <div class="pull-right societies_btn">
                <button id="approve" class ="btn btn-approve" value="<?php echo $societyData[0]['society_id'];?>">Approve</button>
                <button id="reject" class ="btn btn-reject" value="<?php echo $societyData[0]['society_id'];?>">Reject</button>
            </div>
        </div>
    </div>
</div>
</div>

</div>
</div>
</div>
</div>

<script>
    $("#approve").click(function(){
        var society_id = $(this).attr('value');

        $.ajax({
            type     :"POST",
            url      :'<?php echo site_url()."admin/society/approve/"; ?>',
            data     : {society_id: society_id},
            success:function(data)
            { 
                history.go(-1);
            },
        });
    });

    $("#reject").click(function(){
        var society_id = $(this).attr('value');

        $.ajax({
            type     :"POST",
            url      :'<?php echo site_url()."admin/society/reject/"; ?>',
            data     : {society_id: society_id},
            success:function(data)
            { 
                history.go(-1);
            },
        });
    }); 
</script>