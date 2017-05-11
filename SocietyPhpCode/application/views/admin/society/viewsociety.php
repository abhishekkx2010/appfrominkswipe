<style type="text/css">
  .sorting{ text-align: center;}
</style>
<div class="content-wrapper">
  <!-- Content Header (Page header) -->
  <section class="content-header">
   <?php echo $pagetitle; ?>
   <?php echo $breadcrumb; ?>
   <div class="clearfix"></div>  
 </section>
 <!-- Main content -->
 <section class="content">
  <div class="row">
    <!-- left column -->        
    <div class="col-md-12">
     <div class="box box-success">
      <div class="box-header with-border">
       <h3 class="box-title">Society Id : <span><?php echo $viewSociety[0]['id']?></span></h3>
     </div>
     <!-- .box-header -->
     <!-- form start -->
     <div class="box-body">
      <div class="col-md-6">
        <dl class="dl-horizontal">
          <div class="form-group box-header">
            <dt>Society Name :</dt>
            <dd><?php echo $viewSociety[0]['name']?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Address :</dt>
            <dd><?php echo  $viewSociety[0]['address']?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Landmark :</dt>
            <dd><?php echo $viewSociety[0]['landmark']?></dd>
          </div>
          <div class="form-group box-header">
            <dt>City :</dt>
            <dd><?php echo $viewSociety[0]['city']?></dd>
          </div>
          <div class="form-group box-header">
            <dt>State :</dt>
            <dd><?php echo $viewSociety[0]['state']?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Pincode :</dt>
            <dd><?php echo $viewSociety[0]['pincode']?></dd>
          </div>
          <div class="form-group  box-header">
            <dt>Created By :</dt>
            <dd><?php echo $viewSociety[0]['created_by']?></dd>
          </div>
        </dl>
      </div>
      <div class="col-md-6">
       <dl class="dl-horizontal">
        <div class="form-group box-header">
          <dt>Post :</dt>
          <dd><?php if($viewSociety[0]['post'] == 1) {echo "Yes";} else{ echo "No";}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Post Name :</dt>
          <dd><?php echo $viewSociety[0]['post_name']?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Area :</dt>
          <dd><?php echo $viewSociety[0]['area']?></dd>
        </div>                      
        <div class="form-group box-header">
          <dt>Status :</dt>
          <dd><?php if($viewSociety[0]['status'] == 1){echo "Active";} else{echo "Inactive";}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Created Date :</dt>
          <dd><?php echo  $viewSociety[0]['date_created']?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Modified Date :</dt>
          <dd><?php echo  $viewSociety[0]['date_modified']?></dd>
        </div>
      </dl>
    </div>
  </div>
  <div class="box-body">
    <div class="box-header with-border">
      <?php if(!empty($viewSociety[0]['society_image'])){ ?>
        <div class="col-md-6">
          <dl class="dl-horizontal">
            <div class="form-group">
              <dt>Society Image</dt>
              <dd><img src="<?php echo $this->config->item('uploaded_image_url').'images/society_images/'. $viewSociety[0]['society_image'];?>" style="width:304px;height:228px;" class="img-responsive">
              </dd>
            </div>                                           
          </dl>
        </div>
        <?php } ?> 
      </div>
    </div>
    <!-- /.box-body -->  
    <div class="box-footer">&nbsp;</div>          
  </div>
  <div class="clearfix"></div>    
</div>                  
</div>       
</section>
<!-- /.content -->
</div>

