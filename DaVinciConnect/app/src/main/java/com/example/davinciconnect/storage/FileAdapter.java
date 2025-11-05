package com.example.davinciconnect.storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import com.google.firebase.storage.StorageReference;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOLDER = 0;
    private static final int TYPE_FILE = 1;

    public interface OnFileOptionClickListener {
        void onFileClick(StorageReference fileRef);
        void onFolderClick(StorageReference folderRef);
        void onRenameClick(StorageReference itemRef, boolean isFolder);
        void onDeleteClick(StorageReference itemRef, boolean isFolder);
        void onMoveClick(StorageReference itemRef, boolean isFolder);
        void onSendClick(StorageReference fileRef);
    }

    private List<StorageReference> items;
    private OnFileOptionClickListener listener;

    public FileAdapter(List<StorageReference> items, OnFileOptionClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getName().endsWith("/")) {
            return TYPE_FOLDER;
        } else {
            return TYPE_FILE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        if (viewType == TYPE_FOLDER) {
            return new FolderViewHolder(view);
        } else {
            return new FileViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StorageReference item = items.get(position);
        if (holder.getItemViewType() == TYPE_FOLDER) {
            ((FolderViewHolder) holder).bind(item, listener);
        } else {
            ((FileViewHolder) holder).bind(item, listener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void showPopupMenu(View view, StorageReference itemRef, boolean isFolder) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.file_options_menu, popup.getMenu());
        popup.getMenu().findItem(R.id.action_send).setVisible(!isFolder);

        popup.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.action_rename) {
                listener.onRenameClick(itemRef, isFolder);
                return true;
            } else if (itemId == R.id.action_delete) {
                listener.onDeleteClick(itemRef, isFolder);
                return true;
            } else if (itemId == R.id.action_move) {
                listener.onMoveClick(itemRef, isFolder);
                return true;
            } else if (itemId == R.id.action_send) {
                if (!isFolder) listener.onSendClick(itemRef);
                return true;
            }
            return false;
        });
        popup.show();
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        ImageView fileIcon; TextView fileName; ImageButton optionsButton;
        FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileIcon = itemView.findViewById(R.id.ivFileIcon);
            fileName = itemView.findViewById(R.id.tvFileName);
            optionsButton = itemView.findViewById(R.id.btnFileOptions);
        }
        void bind(final StorageReference fileRef, final OnFileOptionClickListener listener) {
            fileName.setText(fileRef.getName());
            fileIcon.setImageResource(R.drawable.ic_file); // <-- Icono local
            itemView.setOnClickListener(v -> listener.onFileClick(fileRef));
            optionsButton.setOnClickListener(v -> showPopupMenu(v, fileRef, false));
        }
    }

    class FolderViewHolder extends RecyclerView.ViewHolder {
        ImageView folderIcon; TextView folderName; ImageButton optionsButton;
        FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folderIcon = itemView.findViewById(R.id.ivFileIcon);
            folderName = itemView.findViewById(R.id.tvFileName);
            optionsButton = itemView.findViewById(R.id.btnFileOptions);
        }
        void bind(final StorageReference folderRef, final OnFileOptionClickListener listener) {
            String name = folderRef.getName().substring(0, folderRef.getName().length() - 1);
            folderName.setText(name);
            folderIcon.setImageResource(R.drawable.ic_folder); // <-- Icono local
            itemView.setOnClickListener(v -> listener.onFolderClick(folderRef));
            optionsButton.setOnClickListener(v -> showPopupMenu(v, folderRef, true));
        }
    }
}
