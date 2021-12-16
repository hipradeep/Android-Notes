   private void setUpPermission() {
        if(ActivityCompat.checkSelfPermission(StudentDashboard.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(StudentDashboard.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(StudentDashboard.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(StudentDashboard.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(StudentDashboard.this,permissionsRequired[3])){
                ActivityCompat.requestPermissions(StudentDashboard.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            } else {
                ActivityCompat.requestPermissions(StudentDashboard.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }
            Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.permissionStatus, true);
        }
    }
