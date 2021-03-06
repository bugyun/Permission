# MissPermission
android 权限库，超级简单好用！！

MissPermission 是精简版本。只有请求权限的简单功能。

### 配置
```xml
implementation 'vip.ruoyun.permission:miss-core:1.0.1'
```

### 准备工作
在 AndroidManifest.xml 文件中添加权限
```xml
<manifest>
    ...
    //添加自己要使用的权限，如果不添加，那么请求这个权限会一直不成功
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.RECEIVE_SMS" />
    <uses-permission android:name="android.permission.SEND_SMS" />

    ...
</manifest>
```

### 使用方法

#### PermissionRequest 方法说明

```java
request.getPermissionList();//此请求的所有权限
request.getDeniedPermissionList();//拒绝的权限
request.getAgreePermissionList();//同意的权限
request.getContext();//上下文
request.isAlwaysDenied();//是否总是拒绝
request.isOver23();//sdk 是否超过 23 (6.0)
request.requestPermissionsAgain();//再次请求权限
```

#### 开始

```java
MissPermission.with(context)//
            .setRequestCode(9898)//默认值 9898
            .addPermission(Manifest.permission.CAMERA)//添加请求权限
            .addPermission(Manifest.permission.SEND_SMS)//添加请求权限
            .addPermission(Manifest.permission.RECEIVE_SMS)//添加请求权限
            .checkPermission(new PermissionRequest.PermissionListener() {
                /**
                   为了适应每种机型弹框提示需要。
                   request.getAgreePermissionList();//同意的权限
                   request.getDeniedPermissionList();//拒绝的权限
                */
                @Override
                public int onChecked(PermissionRequest request) {
                    //3 种返回方式，
                    //MissPermission.NEXT_STEP  直接下一步，不用等待
                    //MissPermission.STOP_STEP  直接停止，不执行下一步
                    //MissPermission.WAIT_STEP  可以做弹出框等操作，然后在按钮点击事件的时候，执行 request.next(); 进行后续操作
                    return MissPermission.WAIT_STEP;
                }

                /**
                   拒绝权限 可以进行提示操作
                   request.isOver23: SDK是否是大约 M
                   request.deniedPermissions: 拒绝的权限
                   request.alwaysDenied: 是否总是拒绝
                */
                @Override
                public void onDenied(PermissionRequest request) {
                    if (request.isAlwaysDenied()) {//是否总是拒绝
                    }

                }

                @Override
                public void onSuccess(PermissionRequest request) {
                    Log.i("zyh", "成功");
                }

                @Override
                public void onFailure(PermissionException exception) {
                    Log.i("zyh", exception.getMessage());
                }
            });
```

需要在 使用方法的 Activity 中重写如下方法:
```java
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    MissPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
}
```


