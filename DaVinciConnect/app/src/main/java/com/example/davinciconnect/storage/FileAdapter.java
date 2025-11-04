package com.example.davinciconnect.storage;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.davinciconnect.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private List<StorageReference> fileList;
    private Context context;

    public FileAdapter(List<StorageReference> fileList, Context context) {
        this.fileList = fileList;
        this.context = context;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        StorageReference file = fileList.get(position);
        String fileName = file.getName();
        int underscoreIndex = fileName.indexOf("_");
        if (underscoreIndex != -1) {
            holder.fileName.setText(fileName.substring(underscoreIndex + 1));
        } else {
            holder.fileName.setText(fileName);
        }


        holder.itemView.setOnClickListener(v -> {
            file.getDownloadUrl().addOnSuccessListener(uri -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "Error al obtener la URL de visualización", Toast.LENGTH_SHORT).show();
            });
        });

        holder.downloadButton.setOnClickListener(v -> {
            file.getDownloadUrl().addOnSuccessListener(uri -> {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file.getName());
                downloadManager.enqueue(request);
                Toast.makeText(context, "Descargando...", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "Error al obtener la URL de descarga", Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        public TextView fileName;
        public Button downloadButton;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.fileName);
            downloadButton = itemView.findViewById(R.id.downloadButton);
        }
    }
}
