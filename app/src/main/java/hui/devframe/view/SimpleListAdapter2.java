package hui.devframe.view;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * 一个简化了BaseAdapter的一部分通用工作的Adapter,如自动处理converView,增加itemType，强制ViewHolder
 * @param <ItemType>
 */
public abstract class SimpleListAdapter2<ItemType,Holder extends SimpleListAdapter2.ViewHolder> extends BaseAdapter {
    public interface ViewHolder{};
    protected Context context;
    /**
     * 存储itemType和LayoutId的map
     */
    private SparseIntArray itemTypeMap;
    private int itemTypeCount;
    /**
     * 当没有多个itemtype的时候，用这个变量存储构造函数传入的layoutId
     */
    private int singleItemLayoutId;
    /**
     * @param context
     * @param itemTypes 如果需要有多个itemType，需要传入一组二位数组，每一个数组有2个字段，第一个是itemType，第二个是对应该itemType的LayoutId
     */
    public SimpleListAdapter2(Context context, int[]... itemTypes) {
        this.context = context;
        isItemTypesValid(itemTypes);
        itemTypeCount = itemTypes.length;
        itemTypeMap = new SparseIntArray(itemTypeCount);
        for(int[] itemType : itemTypes){
            itemTypeMap.put(itemType[0],itemType[1]);
        }
    }
    private static void isItemTypesValid(int[] ... itemTypes){
        //非空检查
        if(itemTypes == null || itemTypes.length == 0){
            throw new RuntimeException("itemTypes不能为空");
        }
        //重复检查（检查传入的itemType中是否有重复，这种不小心的失误可能导致非常难以发现的BUG）
        Set<Integer> itemTypeSet = new HashSet<Integer>();
        Set<Integer> layoutIdSet = new HashSet<Integer>();
        for(int[] itemType : itemTypes){
            //递增检查（itemType必须是从0开始递增，中间不能有跳过的）
            if(itemType[0]< 0 || itemType[0]>=itemTypes.length){
                throw new RuntimeException("itemType的值："+itemType[0]+"超过了itemTypes的总数："+itemTypes.length+"，itemType必须从0开始依次递增");
            }
            itemTypeSet.add(itemType[0]);
            layoutIdSet.add(itemType[1]);
        }
        if(itemTypeSet.size()<itemTypes.length || layoutIdSet.size()<itemTypes.length){
            throw new RuntimeException("已经有相同的itemType存在，请检查构造SimpleListAdapter的itemTypes数组是否有重复");
        }
    }
    /**
     * @param context
     * @param itemLayoutId 用于生成这个Adapter对应的最外层View的LayoutId
     */
    public SimpleListAdapter2(Context context, int itemLayoutId){
        this.context = context;
        singleItemLayoutId = itemLayoutId;
        itemTypeCount = 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public final int getViewTypeCount() {
        return itemTypeCount;
    }
    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        ItemType item = getItem(position);
        if(item == null){
            View empty = new View(this.context);
            empty.setVisibility(View.GONE);
            return empty;
        }
        if(convertView == null){
            int layoutId;
            //区分处理多个itemType和单一itemType的情况
            if(itemTypeMap == null){
                layoutId = singleItemLayoutId;
            }else{
                layoutId = itemTypeMap.get(getItemViewType(position));
                if(layoutId == 0){
                    throw new RuntimeException(String.format("找不到对应的LayoutId(postion=%d itemType=%d),您是否没有覆盖SimpleListAdapter.getItemViewType方法?",position,getItemViewType(position)));
                }
            }
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            holder = onCreateViewHolder(convertView,getItemViewType(position));
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        bindView(position, holder,item);
        return convertView;
    }
    /**
     * 绑定数据的回调
     * @param position
     * @param viewHolder 直接用该viewHolder进行绘制操作
     * @param item 当前的数据对象
     */
    protected abstract void bindView(int position, Holder viewHolder,ItemType item);

    /**
     * 对于给定的view参数，要求子类返回一个对应的ViewHolder对象，在这个函数中完成findViewById的一系列操作
     * @param view
     * @return
     */
    protected abstract Holder onCreateViewHolder(View view,int itemViewType);

    /**
     * 获取对应position位置的数据
     * @param position
     * @return
     */
    @Override
    public abstract ItemType getItem(int position);
}
