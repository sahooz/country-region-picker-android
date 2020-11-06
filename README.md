# country-picker-android

在做app登录的时候，因为需要支持国外手机号注册和登录，所以就涉及到国际电话区号的选择。在github上面找了一下，国家名称基本都是只有英文版本，而手动的去把中文一个个加上实在是一件费时费力的事情，所以就写了一段简单的java代码，抓取了某快递网站的数据转换成json格式，assets/code.json是处理后的数据。    

支持以下语言：  
1. 简体中文
2. 繁体中文
3. 英文  

依赖：
```
implementation 'com.sahooz.library:countrypicker:2.0'
```

提供两个版本的选择器： 

1. DialogFragment版本    
使用示例： 
```java
CountryPickerFragment.newInstance(new PickCountryCallback() {
    @Override
    public void onPick(Country country) {
        if(country.flag != 0) ivFlag.setImageResource(country.flag);
        tvName.setText(country.name);
        tvCode.setText("+" + country.code);
    }
}).show(getSupportFragmentManager(), "country");
```
效果图：  
![img](./imgs/dialogfragment.png)  

2. Activity版本  
使用示例  
```java
startActivityForResult(new Intent(getApplicationContext(), PickActivity.class), 111);

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == 111 && resultCode == Activity.RESULT_OK) {
        Country country = Country.fromJson(data.getStringExtra("country"));
        if(country.flag != 0) ivFlag.setImageResource(country.flag);
        tvName.setText(country.name);
        tvCode.setText("+" + country.code);
    }
}
```
效果图  
![img](./imgs/activity.png)     


另外，使用前和语言发生变化的时候，请初始化：
```java 
// 轻微耗时操作
Country.load(this, Language.SIMPLIFIED_CHINESE);
```  

销毁： 
```java 
Country.destroy();
```

其实代码非常的简单，但是要把各个国家或者地区对应的中英文名称和国旗一一对应起来实在是一件费时费力的事情。供各位参考。