# country-region-picker-android [![](https://jitpack.io/v/sahooz/country-region-picker-android.svg)](https://jitpack.io/#sahooz/country-region-picker-android)

**[English](./README_EN.md)**

在做app登录的时候，因为需要支持国外手机号注册和登录，所以就涉及到国际电话区号的选择。在github上面找了一下，国家名称基本都是只有英文版本，而手动的去把中文一个个加上实在是一件费时费力的事情，所以就写了一段简单的java代码，抓取了某快递网站的数据转换成json格式，assets/code.json是处理后的数据。    

支持以下语言：  
1. 简体中文
2. 繁体中文
3. 英文  
4. n种其他语言

依赖：

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

...

implementation 'com.github.sahooz:country-region-picker-android:3.1.0'
```

提供两个版本的选择器： 

1. DialogFragment版本    
使用示例： 
```java
PickFragment.newInstance(new PickCallback() {
    @Override
    public void onPick(CountryOrRegion countryOrRegion) {
        if(countryOrRegion.flag != 0) ivFlag.setImageResource(countryOrRegion.flag);
        tvName.setText(countryOrRegion.name);
        tvCode.setText("+" + countryOrRegion.code);
    }
}).show(getSupportFragmentManager(), "countryOrRegion");
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
        CountryOrRegion countryOrRegion = CountryOrRegion.fromJson(data.getStringExtra("countryOrRegion"));
        if(countryOrRegion.flag != 0) ivFlag.setImageResource(countryOrRegion.flag);
        tvName.setText(countryOrRegion.name);
        tvCode.setText("+" + countryOrRegion.code);
    }
}
```
效果图  
![img](./imgs/activity.png)     


另外，使用前和语言发生变化的时候，请初始化：
```java 
// 轻微耗时操作
CountryOrRegion.load(this);
```  

销毁： 
```java 
CountryOrRegion.destroy();
```

其实代码非常的简单，但是要把各个国家或者地区对应的中英文名称和国旗一一对应起来实在是一件费时费力的事情。供各位参考。

## 版本更新  

### 3.1.0

1. 国家/地区名相关bug修复
2. 重命名一些包和类，使其更加合理，因为这些并不都是“国家”。所以升级的时候需要注意此部分修改

### 3.0 

1. 更换数据源，修正旧数据存在错误的问题  
2. 增加更多语言支持