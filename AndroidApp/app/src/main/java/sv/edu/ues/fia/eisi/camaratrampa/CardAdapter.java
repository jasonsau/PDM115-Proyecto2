package sv.edu.ues.fia.eisi.camaratrampa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>
implements View.OnClickListener{
    private List<String> urls;
    private List<ImageEntity> imageEntities;
    private View.OnClickListener listener;
    private LayoutInflater layoutInflater;
    private Context context;

    public CardAdapter(Context context, List<ImageEntity> imageEntities){
        this.context = context;
        this.imageEntities = imageEntities;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null) {
            listener.onClick(view);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image_custom);
            textView = view.findViewById(R.id.text_custom);
        }

        public void bindData(ImageEntity imageEntity){
            Picasso.get().load(imageEntity.getUrlFoto())
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView);
            textView.setText(imageEntity.getNombreFoto());
        }

    }

    public void setItems(List<ImageEntity> imageEntities){
        this.imageEntities = imageEntities;
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.activity_card_view_image, null);
        view.setOnClickListener(this);
        return new CardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        holder.bindData(imageEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return this.imageEntities.size();
    }
}
