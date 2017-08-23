
# PinnedHeaderItemDecoration
一个提供吸顶效果的ItemDecoration
现在网上已经有非常多类似这种效果的轮子，那么为什么要重复造这个轮子呢？
因为网上的吸顶效果基本都是基于ItemDecoration绘制的,也就是说基本都是只能看没法操作,只是达到了一个视图的效果。
这样可能不满足一些特定的需求,比如吸顶的这种Type类型的View需要点击怎么办？
基于此种情况,用另一种方式实现了可以点击的吸顶Type.

<br>![image](https://github.com/boboyuwu/PinnedHeaderItemDecoration/blob/master/pic/ScreenGif1.gif)</br>

<br>使用时只需要将你的Adapter实现AdapterStick接口即可,只需要实现这个方法isPinnedViewType()返回需要吸顶的Type
即可实现吸顶效果,所以哪种Type类型需要吸顶效果相关逻辑完全交给使用者去规定，比较灵活，并且由于完全不干涉各种Type类型
布局填充等逻辑,一切还是按照原生Adapter操作，所以可以实现点击事件.如果存在多个Header情况下请额外实现getHeaderCount()
这个方法并返回添加的HeaderView总长度.</br>
<br>
```
    public class SimpleAdapter extends Adapter implements AdapterStick{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    
    //请返回实际HeadView数量
    @Override
    public int getHeaderCount() {
        return 0;
    }

    @Override
    public boolean isPinnedViewType(int viewType) {
        //DO..... 需要吸顶viewType相关逻辑
        
        return false;
    }
}
```
</br>

而在Activity里面这样调用即可
```
  SimpleAdapter simpleAdapter = new SimpleAdapter(this,list);
  recyclerView.addItemDecoration(new PinnedHeaderItemDecoration.Builder().adapterProvider(simpleAdapter).build());
```

<br>依赖方式</br>
1:JitPack
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 ```

 <br>2:dependency</br>
 ```
 	dependencies {
	        compile 'com.github.boboyuwu:PinnedHeaderItemDecoration:V1.0.0'
	}
 ```
