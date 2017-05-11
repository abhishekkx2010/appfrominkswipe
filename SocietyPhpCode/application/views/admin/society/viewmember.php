<style type="text/css">
  .sorting{ text-align: center;}
  button.btn.btn-primary-active.pull-right.in{margin-right: 10px;}
  .btn.btn-primary-active{border-color:#008d4c;background-color:#00a65a;color: #fff;}
  .btn.btn-primary-active:hover{background-color: #008d4c;}
  .btn.btn-primary-delete{background-color: #f44336;color: #fff;}
  .btn.btn-primary-delete:hover{background-color:#d6362a;}
</style>
<div class="content-wrapper">
  <!-- Content Header (Page header) -->
  <section class="content-header">
   <?php echo $pagetitle; ?>
   <?php echo $breadcrumb; ?>
   <div class="clearfix"></div>  
 </section>
 <!-- Main content -->
 <section class="content member_block">
  <div class="row">
    <!-- left column -->        
    <div class="col-md-12">
     <div class="box box-success">
      <div class="box-header with-border">
       <h3 class="box-title">App Member Id : <span><?php if(isset($appUser[0]['id'])){echo $appUser[0]['id'];}?></span></h3>
     </div>
     <!-- .box-header -->
     <!-- form start -->
     <div class="box-body">
      <div class="col-md-6">
        <dl class="dl-horizontal">
          <div class="form-group box-header">
            <dt>Name :</dt>
            <dd><?php if(isset($appUser[0]['name'])){echo $appUser[0]['name'];}?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Email Id :</dt>
            <dd><?php if(isset($appUser[0]['email'])){echo $appUser[0]['email'];}?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Device Token :</dt>
            <dd><p style="word-wrap: break-word;"><?php if(isset($appUser[0]['device_token'])){echo $appUser[0]['device_token'];}?></p></dd>
          </div>
          <div class="form-group box-header">
            <dt>Address :</dt>
            <dd><?php if(isset($appUser[0]['address'])){echo $appUser[0]['address'];}?></dd>
          </div>
          <div class="form-group box-header">
            <dt>City:</dt>
            <dd><?php if(isset($appUser[0]['city'])){echo $appUser[0]['city'];}?></dd>
          </div>
          <div class="form-group box-header">
            <dt>State :</dt>
            <dd><?php if(isset($appUser[0]['state'])){echo $appUser[0]['state'];}?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Created Date:</dt>
            <dd><?php if(isset($appUser[0]['date_created'])){echo $appUser[0]['date_created'];}?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Modified Date:</dt>
            <dd><?php if(isset($appUser[0]['date_modified'])){echo $appUser[0]['date_modified'];}?></dd>
          </div>
        </dl>
      </div>
      <div class="col-md-6">
       <dl class="dl-horizontal">
        <div class="form-group box-header">
          <dt>Gender :</dt>
          <dd><?php if(isset($appUser[0]['gender']))echo $appUser[0]['gender'];?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Mobile Number :</dt>
          <dd><?php if(isset($appUser[0]['mobile'])){echo $appUser[0]['mobile'];}?></dd>
        </div>  
        <div class="form-group box-header">
          <dt>Device Id :</dt>
          <dd><?php if(isset($appUser[0]['device_id'])){echo $appUser[0]['device_id'];}?></dd>
        </div>                       
        <div class="form-group box-header">
          <dt>Device Type :</dt>
          <dd><?php if(isset($appUser[0]['device_type'])){echo $appUser[0]['device_type'];}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Landmark :</dt>
          <dd><?php if(isset($appUser[0]['landmark'])){echo $appUser[0]['landmark'];}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Pincode :</dt>
          <dd><?php if(isset($appUser[0]['pincode'])){echo $appUser[0]['pincode'];}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Status :</dt>
          <dd><?php if($appUser[0]['status'] == 1){echo "Active";} else{echo "Inactive";}?></dd> 
        </div>
        
      </dl>
    </div>
  </div>
  <?php if(!empty($appUser[0]['profile_image'])){ ?>
    <div class="col-md-6">
      <dl class="dl-horizontal">
        <div class="form-group box-header">
          <dt>Profile Image</dt>
          <dd><img src="<?php echo $this->config->item('uploaded_image_url').'images/profile_images/'. $appUser[0]['profile_image'];?>" style="width:304px;height:228px;" class="img-responsive">
          </dd>
        </div>                                           
      </dl>
    </div>
    <?php } ?>

    <?php if(!empty($appUser[0]['cover_image'])){ ?>
      <div class="col-md-6">
       <dl class="dl-horizontal">
        <div class="form-group box-header">
          <dt>Cover image</dt>
          <dd><img src="<?php echo $this->config->item('uploaded_image_url').'images/cover_images/'. $appUser[0]['cover_image'];?>" style="width:304px;height:228px;" class="img-responsive"></dd>
        </div>
      </dl>
    </div>
    <?php } ?>        
    <!-- /.box-body -->  
    <div class="box-footer">&nbsp;</div>          
  </div>
  <div class="clearfix"></div>
  <!-- /.box-body property-->
  <?php foreach ($user_properties as $key => $property) { ?>
   <div class="box box-success">
    <div class="box-header with-border">
     <h3 class="box-title">Society Id : <span><?php if(isset($property['society_id'])){echo $property['society_id'];}?></span></h3>
   </div>
   <div class="box-body">
    <div class="col-md-6">
      <dl class="dl-horizontal">
        <div class="form-group box-header">
          <dt>Society Name :</dt>
          <dd><?php if(isset($property['society_name'])){echo $property['society_name'];}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Landmark :</dt>
          <dd><?php if(isset($property['society_landmark'])){echo $property['society_landmark'];}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Area :</dt>
          <dd><?php if(isset($property['society_area'])){echo $property['society_area'];}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Pincode :</dt>
          <dd><?php if(isset($property['society_pincode'])){echo $property['society_pincode'];}?></dd>
        </div>
      </dl>
    </div>
    <div class="col-md-6">
      <dl class="dl-horizontal">
        <div class="form-group box-header">
          <dt>Address :</dt>
          <dd><?php if(isset($property['society_address'])){echo $property['society_address'];}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>City :</dt>
          <dd><?php if(isset($property['society_city'])){echo $property['society_city'];}?></dd>
        </div>
        <div class="form-group box-header">
          <dt>State :</dt>
          <dd><?php if(isset($property['society_state'])){echo $property['society_state'];}?></dd>
        </div>
      </dl>
    </div>
  </div>
  <div class="box-header with-border">
   <h3 class="box-title">Property Id : <span><?php if(isset($property['id'])){echo $property['id'];}?></span></h3>
 </div>
 <!-- .box-header -->
 <!-- form start -->
 <div class="box-body">
  <div class="col-md-6">
    <dl class="dl-horizontal">
      <div class="form-group box-header">
        <dt>Wing/Building/House Name :</dt>
        <dd><?php if(isset($property['property_name'])){echo $property['property_name'];}?></dd>
      </div>
      <div class="form-group box-header">
        <dt>User type :</dt>
        <dd><?php if(isset($property['user_type'])){echo $property['user_type'];}?></dd>
      </div>
      <div class="form-group box-header">
        <dt>Is Available For Rent :</dt>
        <dd><?php if($property['is_available_for_rent'] == 1){echo "Yes";} else{ echo "No";}?></dd>
      </div>
      <div class="form-group box-header">
        <dt>Status :</dt>
        <dd><?php if($property['status'] == 1){echo "Active";} else{echo "Inactive";}?></dd>
      </div>
      <div class="form-group box-header">
        <dt>Update Date:</dt>
        <dd><?php if(isset($property['updated_on'])){echo $property['updated_on'];}?></dd>
      </div>
    </dl>
  </div>
  <div class="col-md-6">
   <dl class="dl-horizontal">
    <div class="form-group box-header">
      <dt>Property Type :</dt>
      <dd><?php if(isset($property['property_type']))echo $property['property_type'];?></dd>
    </div>
    <div class="form-group box-header">
      <dt>House Number :</dt>
      <dd><?php if(isset($property['house_no'])){echo $property['house_no'];}?></dd>
    </div>  
    <div class="form-group box-header">
      <dt>Rent available Date :</dt>
      <dd><?php if($property['rent_availability_date']){echo $property['rent_availability_date'];} else{echo "Na";}?></dd>
    </div>                       
    <div class="form-group box-header">
      <dt>Created Date :</dt>
      <dd><?php if(isset($property['created_on'])){echo $property['created_on'];}?></dd>
    </div>
  </dl>
</div>
</div>
<?php if(!empty($property['property_image'])){ ?>
  <div class="col-md-6">
    <dl class="dl-horizontal">
      <div class="form-group box-header">
        <dt>Property Image</dt>
        <dd><img src="<?php echo $this->config->item('uploaded_image_url').'images/property_images/'. $property['property_image'];?>" style="width:304px;height:228px;" class="img-responsive">
        </dd>
      </div>                                           
    </dl>
  </div>
  <?php }?>
  <?php if(!empty($property['image_society'])){ ?>
    <div class="col-md-6">
      <dl class="dl-horizontal">
        <div class="form-group box-header">
          <dt>Society Image</dt>
          <dd><img src="<?php echo $this->config->item('uploaded_image_url').'images/society_images/'. $property['image_society'];?>" style="width:304px;height:228px;" class="img-responsive">
          </dd>
        </div>                                           
      </dl>
    </div>
    <?php }?>
    <!-- /.box-body -->  
    <div class="box-footer">
      <div class="col-md-12">
       <a href="javascript:;" class="btn btn-primary-delete pull-right delete_property" value="<?php echo $property['id'];?>">Delete</a>

       <a href="javascript:;" class="property_status" value="<?php echo $property['id']; ?>" data-id="<?php echo $property['status']; ?>">
        <?php if($property['status'] == 1){?>
          <button type="button" class="btn btn-primary-active pull-right in" title="Make Inactive">Inactive</button>
          <?php } else {?>
            <button type="button" class="btn btn-primary-active pull-right in" title="Make Active">Active</button>
            <?php }?>
          </a>
        </div>
      </div>          
    </div>
    <?php }?>
    <!-- .box-header -->    
  </div>   
</div>                 
</section>
<!-- /.content -->
</div>

<script>
  $(".property_status").click(function(){
    var property_id = $(this).attr('value');
    var status = $(this).attr('data-id'); 
    $.ajax({
      type     :"POST",
      url      :'<?php echo site_url()."admin/society/propertyStatus/"; ?>',
      data     : {property_id: property_id,
        status : status
      },
      success:function(data)
      { 
       window.location.reload();
     },
   });  
  });
  $(".delete_property").click(function(){
    var property_id = $(this).attr('value');

    swal({   
      title: "Are you sure?",   
      text: "",   
      type: "warning",   
      showCancelButton: true,   
      confirmButtonColor: "#DD6B55",   
      confirmButtonText: "Delete",   
      closeOnConfirm: false 
    }, function(){   
      $.ajax({
        type     :"POST",
        url      :'<?php echo site_url()."admin/society/deleteProperty/"; ?>',
        data     : {property_id: property_id},
        success:function(data)
        { 
          swal("Deleted!", "", "success"); 
          window.location.reload();
        },
      });
    });
  }); 
</script>
