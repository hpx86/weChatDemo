package com.emmanuel.wechatdemo.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emmanuel.wechatdemo.R;
import com.emmanuel.wechatdemo.bean.Comment;
import com.emmanuel.wechatdemo.bean.ShuoShuo;
import com.emmanuel.wechatdemo.util.ImageLoadUtil;
import com.emmanuel.wechatdemo.view.CommentPopupWindows;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by user on 2016/8/16.
 */
public class FriendsAdapter extends BaseRecycleViewAdapter {

    private final int TYPE_HEAD = 101;
    private final int TYPE_FOOTER = 102;
    private final int TYPE_CONTENT_NORMAL = 1002;
    private final int TYPE_CONTENT_PICTURE = 1003;
    private final int TYPE_CONTENT_VIDEO = 1004;

    private Context context;
    public CommentPopupWindows popupWindow;

    public FriendsAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEAD;
        } else if(position+1 == getItemCount()){
            return TYPE_FOOTER;
        } else {
            if(position >= datas.size())
                return TYPE_CONTENT_NORMAL;
            ShuoShuo shuoShuo = (ShuoShuo) datas.get(position);
            if(shuoShuo.picList != null && shuoShuo.picList.size() >0){
                return TYPE_CONTENT_PICTURE;
            }
            return TYPE_CONTENT_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_HEAD){
            view = LayoutInflater.from(context).inflate(R.layout.item_friends_header, parent, false);
        } else if(viewType == TYPE_FOOTER){
            view = LayoutInflater.from(context).inflate(R.layout.footer_view_load_more, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false);
        }
        SSViewHolder viewHolder = new SSViewHolder(view, viewType);
        popupWindow = new CommentPopupWindows(context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEAD){

        } else if(getItemViewType(position) == TYPE_FOOTER){

        }else {
            int dataIndex = position - 1;
            SSViewHolder viewHolder = (SSViewHolder) holder;
            ShuoShuo shuoShuo = (ShuoShuo) datas.get(dataIndex);
            viewHolder.tvName.setText(shuoShuo.user.name);
            viewHolder.tvContent.setText(shuoShuo.content);
            ImageLoader.getInstance().displayImage(shuoShuo.user.photoUrl, viewHolder.ivPhoto, ImageLoadUtil.getOptions2());
            if (shuoShuo.zanList == null || shuoShuo.zanList.size() <= 0) {
                viewHolder.layoutZans.setVisibility(View.GONE);
                if (shuoShuo.commentList == null || shuoShuo.commentList.size() <= 0) {
                    viewHolder.layoutComment.setVisibility(View.GONE);
                }
            } else {
                viewHolder.layoutZans.setVisibility(View.VISIBLE);
                viewHolder.tvZans.setText(getZansText(shuoShuo.zanList));
            }
            if (!(shuoShuo.commentList == null || shuoShuo.commentList.size() <= 0)) {
                viewHolder.layoutComment.setVisibility(View.VISIBLE);
                //添加评论。。。
            }

            if(getItemViewType(position) == TYPE_CONTENT_PICTURE){
                if(shuoShuo.picList != null){
                    if(shuoShuo.picList.size() > 0){
                        ImageLoader.getInstance().displayImage(shuoShuo.picList.get(0),
                                viewHolder.viewStubIvPic, ImageLoadUtil.getOptions1());
                    }
                }
            }
        }
    }

    private String getZansText(List<String>zans){
        StringBuilder str = new StringBuilder("");
        for(int i=0;i<zans.size();i++){
            str.append(zans.get(0));
            if(i+1 < zans.size()){
                str.append('，');
            }
        }
        return str.toString();
    }

    @Override
    public int getItemCount() {
        return datas.size() + 2;
    }

    public class SSViewHolder extends RecyclerView.ViewHolder{

        public TextView tvName;
        public TextView tvTime;
        public TextView tvContent;
        public TextView tvZans;
        public LinearLayout layoutZans, layoutComment;
        public ImageView ivPhoto, ivComment;
        public ViewStub viewStub;

        public ImageView ivHeader;

        public ImageView viewStubIvPic;
        public GridView viewStubGv;

        public SSViewHolder(View itemView, int itemType) {
            super(itemView);
            if (itemType == TYPE_HEAD){

            } else if(itemType == TYPE_FOOTER){

            } else {
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
                tvTime = (TextView) itemView.findViewById(R.id.tv_msg_time);
                tvContent = (TextView) itemView.findViewById(R.id.tv_content);
                ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);

                layoutZans = (LinearLayout) itemView.findViewById(R.id.layout_zans);
                tvZans = (TextView) itemView.findViewById(R.id.tv_zans);
                layoutComment = (LinearLayout) itemView.findViewById(R.id.layout_comment);
                ivComment = (ImageView) itemView.findViewById(R.id.btn_comment);
                ivComment.setOnClickListener(onClickListener);
                viewStub = (ViewStub)itemView.findViewById(R.id.viewStub);
                if(itemType == TYPE_CONTENT_PICTURE){
                    viewStub.setLayoutResource(R.layout.view_stub_picture);
                    viewStub.inflate();
                    viewStubGv = (GridView)itemView.findViewById(R.id.gr_picture);
                    viewStubIvPic = (ImageView)itemView.findViewById(R.id.iv_picture);
                }
            }

        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_comment:
                    popupWindow.showLeft(view);
                    break;
            }
        }
    };
}