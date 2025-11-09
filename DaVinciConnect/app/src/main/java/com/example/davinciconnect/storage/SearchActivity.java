package com.example.davinciconnect.storage;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements FileAdapter.OnFileOptionClickListener {

    private RecyclerView rvSearchResults;
    private FileAdapter fileAdapter;
    private List<StorageReference> searchResults;
    private StorageReference userRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            finish();
            return;
        }
        userRootRef = FirebaseStorage.getInstance().getReference("users").child(currentUser.getUid());

        rvSearchResults = findViewById(R.id.rvSearchResults);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        searchResults = new ArrayList<>();
        fileAdapter = new FileAdapter(searchResults, this);
        rvSearchResults.setAdapter(fileAdapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    searchResults.clear();
                    fileAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    private void performSearch(String query) {
        searchResults.clear();
        userRootRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference folderRef : listResult.getPrefixes()) {
                if (!folderRef.getName().equals("Privado")) {
                    searchInFolder(folderRef, query);
                }
            }
        });
    }

    private void searchInFolder(StorageReference folderRef, String query) {
        folderRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference itemRef : listResult.getItems()) {
                if (itemRef.getName().toLowerCase().contains(query.toLowerCase())) {
                    searchResults.add(itemRef);
                }
            }
            for (StorageReference subFolderRef : listResult.getPrefixes()) {
                if (subFolderRef.getName().toLowerCase().contains(query.toLowerCase())) {
                    searchResults.add(subFolderRef);
                }
                searchInFolder(subFolderRef, query);
            }
            fileAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onFileClick(StorageReference fileRef) {
        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    @Override
    public void onFolderClick(StorageReference folderRef) {
        Intent intent = new Intent(this, FileListActivity.class);
        intent.putExtra("FOLDER_NAME", folderRef.getName());
        startActivity(intent);
    }

    // Implement other methods from FileAdapter.OnFileOptionClickListener as needed, possibly with no-ops
    @Override
    public void onMoveClick(StorageReference itemToMove, boolean isFolder) {}

    @Override
    public void onRenameClick(StorageReference itemRef, boolean isFolder) {}

    @Override
    public void onDeleteClick(StorageReference itemRef, boolean isFolder) {}

    @Override
    public void onSendClick(StorageReference fileRef) {}
}
