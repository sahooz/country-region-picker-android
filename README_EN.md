# countryOrRegion-picker-android [![](https://jitpack.io/v/sahooz/countryOrRegion-picker-android.svg)](https://jitpack.io/#sahooz/countryOrRegion-picker-android)

**[中文说明](./README.md)**  

Languages supported：  
1. Simplify Chinese
2. Tradictional Chinese
3. English  
4. Many other languages

Add dependency：

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

...

implementation 'com.github.sahooz:countryOrRegion-picker-android:3.0.0'
```

The two optional pickers： 

1. DialogFragment version    
Sample： 
```java
CountryFragment.newInstance(new PickCallback() {
    @Override
    public void onPick(CountryOrRegion countryOrRegion) {
        if(countryOrRegion.flag != 0) ivFlag.setImageResource(countryOrRegion.flag);
        tvName.setText(countryOrRegion.name);
        tvCode.setText("+" + countryOrRegion.code);
    }
}).show(getSupportFragmentManager(), "countryOrRegion");
```
What it looks like：  
![img](./imgs/dialogfragment.png)  

2. Activity version  
Sample  
```java
startActivityForResult(new Intent(getApplicationContext(), PickActivity.class), 111);

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == 111 && resultCode == Activity.RESULT_OK) {
        CountryOrRegion countryOrRegion = Country.fromJson(data.getStringExtra("countryOrRegion"));
        if(countryOrRegion.flag != 0) ivFlag.setImageResource(countryOrRegion.flag);
        tvName.setText(countryOrRegion.name);
        tvCode.setText("+" + countryOrRegion.code);
    }
}
```
What it looks like：  
![img](./imgs/activity.png)     


When the device language is changed, do this：
```java 
// Take a little while
CountryOrRegion.load(this);
```  

Destroy it after usage： 
```java 
CountryOrRegion.destroy();
```

The code is very simple, but supporting multiple languages is very time consuming. Hope it help.

## Update Logs  

### 3.1.03

1. Country/Region name bug fix
2. Rename some packages and classes and make it reasonable, cause not every subject in the list is a country

### 3.0  

1. Update datasource, fix old data errors  
2. Sopport more langaues