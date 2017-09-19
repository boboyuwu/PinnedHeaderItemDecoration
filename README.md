
# PinnedHeaderItemDecoration
一个提供吸顶效果的ItemDecoration
现在网上有很多类似这种效果的轮子，那么为什么还要重复造这个轮子呢？
<br>原因主要有2个：</br>
<br>1:网上的吸顶效果基本都是基于ItemDecoration绘制的,也就是说只能看没法操作,只是达到了一个视图的效果。
这样可能不满足一些特定的需求,比如吸顶的这种Type类型的View需要点击处理怎么办？</br>
<br>2:这种基于ItemDecoration绘制使用起来会有很大的不便,我们需要在源码中去更改样式并且必须实现它提供的一大堆方法,非常烦恼</br>
<br>基于以上情况,所以我开发了这款既简单操作又完美解决上述问题自由设置吸顶Type的PinnedHeaderItemDecoration.</br>

PinnedHeaderItemDecoration支持LinearLayoutManager和GridLayoutManager,目前只支持不带Header以及带Header吸顶效果,
后续会支持分割线。

<br>![image](https://github.com/boboyuwu/PinnedHeaderItemDecoration/blob/master/pic/ScreenGif2.gif)</br>

<br>PinnedHeaderItemDecoration上手使用非常非常简单,使用时只需要将你的Adapter实现AdapterStick接口即可,
默认只需要实现这个方法isPinnedViewType()返回需要吸顶的Type即可实现吸顶效果,所以哪种Type类型需要吸顶效果
相关逻辑处理完全交给使用者去决定，这样比较灵活，并且由于完全不干涉各种Type类型布局填充等逻辑,一切还是按照
原生Adapter操作，所以可以实现点击事件等操作.如果存在多个Header情况请额外实现getHeaderCount()方法并返回
添加的HeaderView总长度.</br>
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

而在Activity里面这样调用即可,非常简单~
```
  SimpleAdapter simpleAdapter = new SimpleAdapter(this,list);
  recyclerView.addItemDecoration(PinnedHeaderItemDecoration.builder().adapterProvider(simpleAdapter).build());
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
	          compile 'com.github.boboyuwu:PinnedHeaderItemDecoration:V1.0.2'
	      }
 ```
