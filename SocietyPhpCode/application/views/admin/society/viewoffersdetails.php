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
       <h3 class="box-title">Offer Id : <span><?php echo $offerData[0]['offer_id']?></span></h3>
     </div>
     <div class="box-body">
      <div class="col-md-6">
        <dl class="dl-horizontal">
          <div class="form-group box-header">
            <dt>Shared With :</dt>
            <dd><?php echo $offerData[0]['shared_with']?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Company Name :</dt>
            <dd><?php echo $offerData[0]['company_name']?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Offer Title :</dt>
            <dd><?php echo $offerData[0]['title']?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Offer Description :</dt>
            <dd><?php echo $offerData[0]['description']?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Offer category :</dt>
            <dd><?php echo $offerData[0]['category']?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Contact Person Name:</dt>
            <dd><?php echo $offerData[0]['contact_person_name']?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Contact Number:</dt>
            <dd><?php echo $offerData[0]['contact_person_mobile']?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Email Id:</dt>
            <dd><?php echo $offerData[0]['contact_person_email']?></dd>
          </div>
          <?php if(!empty($offerData[0]['creator_notes'])){
             ?> <div class="form-group box-header">
            <dt>Creator Notes:</dt>
            <dd><?php echo $offerData[0]['creator_notes']?></dd>
          </div>  
           <?php }?>
          
        </dl>
      </div>
      <div class="col-md-6">
       <dl class="dl-horizontal">
        <div class="form-group box-header">
          <dt>Created By :</dt>
          <dd><?php echo ucfirst($username)?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Company Address:</dt>
          <dd><?php echo $offerData[0]['company_address']?></dd>
        </div> 
        <?php if(!empty($offerData[0]['amount'])){ ?> 
        <div class="form-group box-header">
          <dt>Amount :</dt>
          <dd><?php echo $offerData[0]['amount']?></dd>
        </div>
        <?php }?>
        <div class="form-group box-header">
          <dt>URL :</dt>
          <dd><p style="word-wrap: break-word;"><?php echo $offerData[0]['url']?></p></dd>
        </div>                       
        <div class="form-group box-header">
          <dt>Offer Start Date :</dt>
          <dd><?php echo $offerData[0]['start_date']?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Offer Expiry Date :</dt>
          <dd><?php echo $offerData[0]['expire_on']?></dd>
        </div>
        <div class="form-group box-header">
          <dt>Status :</dt>
          <dd><?php if($offerData[0]['offer_status'] == 0 || $offerData[0]['offer_status'] == "") {

            echo "Inactive";} else{echo "Active";} ?></dd>
          </div>
          <div class="form-group box-header">
            <dt>Created Date :</dt>
            <dd><?php echo  $offerData[0]['created_on']?></dd>
          </div>
        </dl>
      </div>
    </div>
    <div class="box-footer">&nbsp;</div>          
  </div>
  <div class="clearfix"></div>
  <?php if($offerData[0]['shared_with'] == "Private") {?>
   <div class="box box-success">
    <div class="box-header with-border">
     <h3 class="box-title">Society Details:</h3>
   </div>
   <div class="box-body">
    <div class="col-md-12 table-responsive">
      <dl class="dl-horizontal">
        <table id="example1" class="table table-bordered table-striped dataTable ztp_detection_table" role="grid" aria-describedby="example1_info">
          <thead>
            <tr role="row" class="input_width">
              <th  class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">Society Id</th>
              <th  class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">Society Name</th>
              <th  class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">Address</th>
              <th  class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">Landmark</th>
              <th  class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">City</th>
              <th  class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">Pin Code</th>
              <th  class="sorting" tabindex="0" aria-controls="example1" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">State</th>
            </tr>
          </thead>
          <tbody>
            <?php foreach ($offerData as $key => $offers) { ?>
              <tr class="text-center">
                <td><?php echo $offers['society_id'] ?></td>
                <td><?php echo $offers['name'] ?></td>
                <td><?php echo $offers['address'] ?></td>
                <td><?php echo $offers['landmark'] ?></td>
                <td><?php echo $offers['city'] ?></td>
                <td><?php echo $offers['pincode'] ?></td>
                <td><?php echo $offers['state'] ?></td>
              </tr>
              <?php }?>
            </tbody>
          </table>
        </dl>
      </div>            
    </div>
    <!-- /.box-body -->
    <div class="box-footer">&nbsp;</div>              
  </div>
  <?php }?>
  <div class="clearfix"></div>

  <div class="box box-success">
    <div class="box-header with-border">
     <h3 class="box-title">Images</h3>
   </div>
   <!-- .box-header -->
   <!-- form start -->
   <div class="box-body">
    <?php if(!empty($offerData[0]['offer_logo'])){ ?>
      <div class="col-md-6">
        <dl class="dl-horizontal">
          <div class="form-group">
            <dt>Offer Logo</dt>
            <dd><img src="<?php echo $this->config->item('uploaded_image_url').'images/offer_images/'. $offerData[0]['offer_logo'];?>" style="width:200px;height:200px;" class="img-responsive">
            </dd>
          </div>                                           
        </dl>
      </div>
      <?php } ?>

      <?php if(!empty($offerData[0]['offer_image'])){ ?>
        <div class="col-md-6">
         <dl class="dl-horizontal">
          <div class="form-group">
            <dt>Offer Image</dt>
            <dd><img src="<?php echo $this->config->item('uploaded_image_url').'images/offer_images/'. $offerData[0]['offer_image'];?>" style="width:400px;height:250px;" class="img-responsive"></dd>
          </div>
        </dl>
      </div>
      <?php } ?>               
    </div>
    <!-- /.box-body -->
    <div class="box-footer">&nbsp;</div>              
  </div>
  <div class="clearfix"></div>
  <!-- /.box -->        
</div>               
</div>       
</section>
<!-- /.content -->
</div>

