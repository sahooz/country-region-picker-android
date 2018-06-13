package com.sahooz.library;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by android on 3/15/2018.
 */

public abstract class PyAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements View.OnClickListener {

    private static final String TAG = PyAdapter.class.getSimpleName();
    public static final int TYPE_LETTER = 0;
    public static final int TYPE_OTHER = 1;
    private WeakHashMap<View, VH> holders = new WeakHashMap<>();
    public final ArrayList<PyEntity> entityList = new ArrayList<>();
    public final  HashSet<LetterEntity> letterSet = new HashSet<>();
    private OnItemClickListener listener = (entity, position) -> {

    };

    public PyAdapter(List<? extends PyEntity> entities){
        if(entities == null) throw new NullPointerException("entities == null!");
        update(entities);
    }

    public void update(List<? extends PyEntity> entities){
        if(entities == null) throw new NullPointerException("entities == null!");
        entityList.clear();
        entityList.addAll(entities);
        letterSet.clear();
        for (PyEntity entity : entities) {
            String pinyin = entity.getPinyin();
            if(!TextUtils.isEmpty(pinyin)) {
                char letter = pinyin.charAt(0);
                if(!isLetter(letter))
                    letter = 35;
                letterSet.add(new LetterEntity(letter + ""));
            }
        }
        entityList.addAll(letterSet);
        Collections.sort(entityList, (o1, o2) -> {
            String pinyin = o1.getPinyin().toLowerCase();
            String anotherPinyin = o2.getPinyin().toLowerCase();
            char letter = pinyin.charAt(0);
            char otherLetter = anotherPinyin.charAt(0);
            if(isLetter(letter) && isLetter(otherLetter))
                return pinyin.compareTo(anotherPinyin);
            else if(isLetter(letter) && !isLetter(otherLetter)) {
                return -1;
            } else if(!isLetter(letter) && isLetter(otherLetter)){
                return 1;
            } else {
                if(letter == 35 && o1 instanceof LetterEntity) return -1;
                else if(otherLetter == 35 && o2 instanceof LetterEntity) return 1;
                else return pinyin.compareTo(anotherPinyin);
            }
        });
        notifyDataSetChanged();
    }

    private boolean isLetter(char letter) {
        return 'a' <= letter && 'z' >= letter || 'A' <= letter && 'Z' >= letter;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        PyEntity entity = entityList.get(position);
        holders.put(holder.itemView, holder);
        holder.itemView.setOnClickListener(this);
        if(entity instanceof LetterEntity) {
            onBindLetterHolder(holder, (LetterEntity)entity, position);
        } else {
            onBindHolder(holder, entity, position);
        }
    }

    public int getEntityPosition(PyEntity entity) { return entityList.indexOf(entity); }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == TYPE_LETTER? onCreateLetterHolder(parent, viewType)
                : onCreateHolder(parent, viewType);
    }

    public abstract VH onCreateLetterHolder(ViewGroup parent, int viewType);
    public abstract VH onCreateHolder(ViewGroup parent, int viewType);

    public int getLetterPosition(String letter){
        LetterEntity entity = new LetterEntity(letter);
        return entityList.indexOf(entity);
    }

    @Override
    public int getItemViewType(int position) {
        PyEntity entity = entityList.get(position);
        return entity instanceof LetterEntity? TYPE_LETTER : getViewType(entity, position);
    }

    public int getViewType(PyEntity entity, int position) {
        return TYPE_OTHER;
    }

    @Override
    public final int getItemCount() { return entityList.size(); }

    public void onBindLetterHolder(VH holder, LetterEntity entity, int position) {

    }

    public void onBindHolder(VH holder, PyEntity entity, int position) {

    }

    public boolean isLetter(int position) {
        if(position < 0 || position >= entityList.size()) return false;
        else return entityList.get(position) instanceof LetterEntity;
    }

    public interface OnItemClickListener {
        void onItemClick(PyEntity entity, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public final void onClick(View v) {
        VH holder = holders.get(v);
        if(holder == null) {
            Log.e(TAG, "Holder onClick event, but why holder == null?");
            return;
        }
        int position = holder.getAdapterPosition();
        PyEntity pyEntity = entityList.get(position);
        listener.onItemClick(pyEntity, position);
    }

    public static final class LetterEntity implements PyEntity {

        public final String letter;

        public LetterEntity(String letter) { this.letter = letter; }

        @Override @NonNull
        public String getPinyin() { return letter.toLowerCase(); }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LetterEntity that = (LetterEntity) o;

            return letter.toLowerCase().equals(that.letter.toLowerCase());
        }

        @Override
        public int hashCode() { return letter.toLowerCase().hashCode(); }
    }
}
