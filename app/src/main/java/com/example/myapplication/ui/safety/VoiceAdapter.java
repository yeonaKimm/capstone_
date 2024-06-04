package com.example.myapplication.ui.safety;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class VoiceAdapter extends RecyclerView.Adapter<VoiceAdapter.VoiceViewHolder> {

    private final List<Voice_Item_Safety> voiceItemList;
    private final Context context;
    private MediaPlayer mediaPlayer;

    public VoiceAdapter(Context context, List<Voice_Item_Safety> voiceItemList) {
        this.context = context;
        this.voiceItemList = voiceItemList;
    }

    @NonNull
    @Override
    public VoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.safety_item_voice, parent, false);
        return new VoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoiceViewHolder holder, int position) {
        Voice_Item_Safety item = voiceItemList.get(position);
        holder.voiceTitle.setText(item.getTitle());
        holder.playIcon.setOnClickListener(v -> playSound(holder, item.getSoundResId()));
    }

    @Override
    public int getItemCount() {
        return voiceItemList.size();
    }

    private void playSound(VoiceViewHolder holder, int soundResId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(context, soundResId);
        mediaPlayer.setOnPreparedListener(mp -> {
            holder.playIcon.setImageResource(R.drawable.ic_play); // 재생 중 아이콘으로 변경
            mp.start();
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            holder.playIcon.setImageResource(R.drawable.ic_voice); // 재생 완료 후 원래 아이콘으로 변경
            mp.release();
            mediaPlayer = null;
        });

        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            holder.playIcon.setImageResource(R.drawable.ic_error); // 에러 발생 시 원래 아이콘으로 변경
            mp.release();
            mediaPlayer = null;
            return true;
        });
    }

    static class VoiceViewHolder extends RecyclerView.ViewHolder {
        TextView voiceTitle;
        ImageView playIcon;

        public VoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            voiceTitle = itemView.findViewById(R.id.voice_title);
            playIcon = itemView.findViewById(R.id.play_icon);
        }
    }
}
