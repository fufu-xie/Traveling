package com.yu.hu.traveling.yonglian.custom;

import android.view.LayoutInflater;
import android.view.View;

import com.yu.hu.traveling.R;
import com.yuntongxun.plugin.im.ui.chatting.holder.BaseHolder;
import com.yuntongxun.plugin.im.ui.chatting.model.BaseChattingRow;
import com.yuntongxun.plugin.im.ui.chatting.view.ChattingItemContainer;


/**
 * Created with Android Studio IDEA.
 *
 * @author WJ
 * @version 1.0
 * @since 2017/8/21 14:13
 */

public class CustomTxRow extends BaseChattingRow {

    public CustomTxRow(int type) {
        super(type);
    }

    @Override
    protected void buildChattingData() {
        CustomViewHolder holder = (CustomViewHolder) mBaseHolder;
        if (holder != null) {
            holder.init(mFragment, mMessage, position);
        }

        // 显示消息状态
        getMsgStateResId(mFragment, position, holder, mMessage, mFragment.getOnClickListener());
    }

    @Override
    public View buildChatView(LayoutInflater inflater, View convertView) {
        //we have a don't have a converView so we'll have to create a new one

        if (convertView == null || ((BaseHolder) convertView.getTag()).getType() != mRowType) {
            // convertView = inflater.inflate(R.layout.ytx_chatting_custom_item_to, null);
            // 加载to布局第三个参数为true表示不要添加已读未读的View
            convertView = new ChattingItemContainer(inflater, R.layout.ytx_chatting_custom_item_to, true);

            //use the view holder pattern to save of already looked up subviews
            CustomViewHolder holder = new CustomViewHolder(mRowType);
            convertView.setTag(holder.initBaseHolder(convertView));
        }

        return convertView;
    }

    @Override
    public int getChatViewType() {
        return CustomRowUtil.KEY_100;
    }
}