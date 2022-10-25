package com.example.heychat.adapters;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.Toast;
=======
import android.widget.TextView;
>>>>>>> 8e766fd421345a25f6ea4e1f1d73b281b5c00909

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heychat.R;
import com.example.heychat.databinding.ItemContainerReceivedMessageBinding;
import com.example.heychat.databinding.ItemContainerSentMessageBinding;
import com.example.heychat.listeners.MessageListener;
import com.example.heychat.models.ChatMessage;
import com.example.heychat.ultilities.Constants;
<<<<<<< HEAD
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
=======
import com.google.android.gms.dynamic.IFragmentWrapper;
>>>>>>> 8e766fd421345a25f6ea4e1f1d73b281b5c00909

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

<<<<<<< HEAD
    public  List<ChatMessage> chatMessages;
    private Bitmap receiverProfileImage;
    private final String senderId;
    public  MessageListener messageListener;
=======
    public List<ChatMessage> chatMessages;
    private Bitmap receiverProfileImage;
    private final String senderId;
    public MessageListener messageListener;
>>>>>>> 8e766fd421345a25f6ea4e1f1d73b281b5c00909

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_SENT_IMAGE = 3;
    private static final int VIEW_TYPE_RECEIVED_IMAGE = 4;
    private static final int VIEW_TYPE_SENT_FILE = 5;
    private static final int VIEW_TYPE_RECEIVED_FILE = 6;

    public void setReceiverProfileImage(Bitmap bitmap) {
        receiverProfileImage = bitmap;
    }

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderId, MessageListener messageListener) {

        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
        this.chatMessages = chatMessages;
        this.messageListener = messageListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT || viewType == VIEW_TYPE_SENT_IMAGE || viewType == VIEW_TYPE_SENT_FILE) {
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else {
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setTextData(chatMessages.get(position));
        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVED) {
            ((ReceivedMessageViewHolder) holder).setTextData(chatMessages.get(position), receiverProfileImage);
        } else if (getItemViewType(position) == VIEW_TYPE_SENT_IMAGE) {
            ((SentMessageViewHolder) holder).setImageData(chatMessages.get(position));
        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVED_IMAGE) {
            ((ReceivedMessageViewHolder) holder).setImageData(chatMessages.get(position), receiverProfileImage);
        } else if (getItemViewType(position) == VIEW_TYPE_SENT_FILE) {
            ((SentMessageViewHolder) holder).setFileData(chatMessages.get(position));
        } else if (getItemViewType(position) == VIEW_TYPE_RECEIVED_FILE) {
            ((ReceivedMessageViewHolder) holder).setFileData(chatMessages.get(position), receiverProfileImage);
        }

//        if (chatMessages.get(0).model.equals("receiver")) {
//            for (int i = 0; i < chatMessages.size() - 1; i++) {
//                if (chatMessages.get(i).model.equals("receiver")) {
//                    if (chatMessages.get(i + 1).model.equals("sender")) {
//                        chatMessages.get(i).lastReceiver = true;
//                    }
//                }
//            }
//        } else {
//            for (int i = chatMessages.size() - 2; i >= 0; i--){
//                if (chatMessages.get(i).model.equals("receiver")) {
//                    if (chatMessages.get(i + 1).model.equals("sender")) {
//                        chatMessages.get(i).lastReceiver = true;
//                    }
//                }
//            }
//        }


    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).type != null) {
            if (chatMessages.get(position).senderId.equals(senderId)) {
                if (chatMessages.get(position).type.equals(Constants.MESSAGE_TEXT)) {
                    return VIEW_TYPE_SENT;
                } else if (chatMessages.get(position).type.equals(Constants.MESSAGE_IMAGE)) {
                    return VIEW_TYPE_SENT_IMAGE;
                } else if (chatMessages.get(position).type.equals(Constants.MESSAGE_FILE)){
                    return VIEW_TYPE_SENT_FILE;
                }

            } else {
                if (chatMessages.get(position).type.equals(Constants.MESSAGE_IMAGE)) {
                    return VIEW_TYPE_RECEIVED_IMAGE;
                } else if (chatMessages.get(position).type.equals(Constants.MESSAGE_FILE)){
                    return VIEW_TYPE_RECEIVED_FILE;
                }
            }
        }

            if (chatMessages.get(position).model.equals("receiver")) {
                if (position != 0) {
                    if (chatMessages.get(position - 1).model.equals("sender")) {
                        chatMessages.get(position).lastReceiver = true;
                    }
                }
            }

            if (chatMessages.get(0).model.equals("receiver"))
                chatMessages.get(0).lastReceiver = true;


        return VIEW_TYPE_RECEIVED;
    }

    class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;

            itemView.setOnClickListener(view -> {
                if (messageListener != null) {
                    int pos = getAdapterPosition();
                    chatMessages.get(pos).isSelected = !chatMessages.get(pos).isSelected;
                    if (!chatMessages.get(pos).isSelected) {
                        binding.txtDelete.setVisibility(View.GONE);
                        binding.textDateTime.setVisibility(View.GONE);
                        if (!chatMessages.get(pos).lastReceiver)
                            binding.textDateTime.setVisibility(View.GONE);
                    } else {
                        binding.txtDelete.setVisibility(View.VISIBLE);
                        binding.textDateTime.setVisibility(View.VISIBLE);
                        if (!chatMessages.get(pos).lastReceiver)
                            binding.textDateTime.setVisibility(View.VISIBLE);
                    }
                    messageListener.onMessageSelection(chatMessages.get(pos).isSelected);
                }
            });

            binding.txtDelete.setOnClickListener(view -> {
                if (messageListener != null) {
                    int pos = getAdapterPosition();
                    messageListener.onDeleteMessage(chatMessages.get(pos), pos, chatMessages);
                }
            });

        }

        void setTextData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textMessage.setVisibility(ViewGroup.VISIBLE);
            binding.textDateTime.setText(chatMessage.dateTime);
        }

        void setImageData(ChatMessage chatMessage) {
            binding.layoutMessage.setBackground(null);
            binding.imageMessage.setImageBitmap(getUserImage(chatMessage.message));
            binding.imageMessage.setVisibility(ViewGroup.VISIBLE);
            binding.textDateTime.setText(chatMessage.dateTime);
        }

<<<<<<< HEAD
        void setFileData(ChatMessage chatMessage){
            try {
//                URL fileUrl = new URL(chatMessage.message);
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(chatMessage.message);
                String fileName = storageReference.getName().toString();
                String extension = fileName.substring(fileName.lastIndexOf(".")+1);
                String name = fileName.split("--__")[0];
                binding.textMessage.setText(name+"."+extension);
                binding.textMessage.setVisibility(ViewGroup.VISIBLE);
                binding.textDateTime.setText(chatMessage.dateTime);
                binding.downBtn.setVisibility(ViewGroup.VISIBLE);

                binding.downBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downloadFile(view.getContext(), name+"."+extension, chatMessage.message);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private static Bitmap getUserImage(String encodedImage) {
=======
        private Bitmap getUserImage(String encodedImage) {
>>>>>>> 8e766fd421345a25f6ea4e1f1d73b281b5c00909
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        private void downloadFile(Context context, String fileName, String url){
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/HeyChat");
            if (!folder.exists()){
                folder.mkdir();
            }

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            request.setDestinationInExternalPublicDir(name, fileName);
//            request.setDestinationInExternalPublicDir("/Download/HeyChat pdf", fileName);

            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/HeyChat/", fileName);
            request.setDestinationUri(Uri.fromFile(file));
            downloadManager.enqueue(request);
        }

    }

    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
            itemView.setOnClickListener(view -> {
<<<<<<< HEAD
                if (messageListener != null){
                    int pos = getAdapterPosition();
                    chatMessages.get(pos).isSelected = !chatMessages.get(pos).isSelected;
                    if (!chatMessages.get(pos).isSelected)
                        binding.txtTranslate.setVisibility(View.GONE);
                    else binding.txtTranslate.setVisibility(View.VISIBLE);
=======
                if (messageListener != null) {
                    int pos = getAdapterPosition();
                    chatMessages.get(pos).isSelected = !chatMessages.get(pos).isSelected;
                    if (!chatMessages.get(pos).isSelected) {
                        binding.txtTranslate.setVisibility(View.GONE);
                        binding.txtDelete.setVisibility(View.GONE);
                        binding.textDateTime.setVisibility(View.GONE);
                    } else {
                        binding.txtTranslate.setVisibility(View.VISIBLE);
                        binding.txtDelete.setVisibility(View.VISIBLE);
                        binding.textDateTime.setVisibility(View.VISIBLE);
                    }
>>>>>>> 8e766fd421345a25f6ea4e1f1d73b281b5c00909
                    messageListener.onMessageSelection(chatMessages.get(pos).isSelected);
                }
            });
            binding.txtTranslate.setOnClickListener(view -> {
<<<<<<< HEAD
                if (messageListener != null){
                    int pos = getAdapterPosition();
                    messageListener.onGetMessage(chatMessages.get(pos));
=======
                if (messageListener != null) {
                    int pos = getAdapterPosition();
                    messageListener.onTranslateMessage(chatMessages.get(pos), pos);
                }
            });

            binding.txtDelete.setOnClickListener(view -> {
                if (messageListener != null) {
                    int pos = getAdapterPosition();
                    messageListener.onDeleteMessage(chatMessages.get(pos), pos, chatMessages);
>>>>>>> 8e766fd421345a25f6ea4e1f1d73b281b5c00909
                }
            });
        }

        void setTextData(ChatMessage chatMessage, Bitmap receiverProfileImage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textMessage.setVisibility(View.VISIBLE);
            binding.textDateTime.setText(chatMessage.dateTime);

            if (receiverProfileImage != null) {
                binding.imageProfile.setImageBitmap(receiverProfileImage);
            }
<<<<<<< HEAD
=======
            if (chatMessage.lastReceiver) {
                binding.imageProfile.setVisibility(View.VISIBLE);
            } else {
                binding.imageProfile.setVisibility(View.GONE);
            }
>>>>>>> 8e766fd421345a25f6ea4e1f1d73b281b5c00909

        }

        void setImageData(ChatMessage chatMessage, Bitmap receiverProfileImage) {
            binding.layoutMessage.setBackground(null);
            binding.imageMessage.setImageBitmap(getUserImage(chatMessage.message));
            binding.imageMessage.setVisibility(View.VISIBLE);
            binding.textDateTime.setText(chatMessage.dateTime);

            if (receiverProfileImage != null) {
                binding.imageProfile.setImageBitmap(receiverProfileImage);
            }
        }

<<<<<<< HEAD
        void setFileData(ChatMessage chatMessage, Bitmap receiverProfileImage){
            try {
//                URL fileUrl = new URL(chatMessage.message);
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(chatMessage.message);
                String fileName = storageReference.getName().toString();
                String extension = fileName.substring(fileName.lastIndexOf(".")+1);
                String name = fileName.split("--__")[0];
                binding.textMessage.setText(name+"."+extension);
                binding.textMessage.setVisibility(ViewGroup.VISIBLE);
                binding.textDateTime.setText(chatMessage.dateTime);
                binding.downBtn.setVisibility(ViewGroup.VISIBLE);

                binding.downBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downloadFile(view.getContext(), name+"."+extension, chatMessage.message);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (receiverProfileImage != null) {
                binding.imageProfile.setImageBitmap(receiverProfileImage);
            }
        }

=======
>>>>>>> 8e766fd421345a25f6ea4e1f1d73b281b5c00909
        private Bitmap getUserImage(String encodedImage) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        private void downloadFile(Context context, String fileName, String url){
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/HeyChat");
            if (!folder.exists()){
                folder.mkdir();
            }

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            request.setDestinationInExternalPublicDir(name, fileName);
//            request.setDestinationInExternalPublicDir("/Download/HeyChat pdf", fileName);

            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/HeyChat/", fileName);
            request.setDestinationUri(Uri.fromFile(file));
            downloadManager.enqueue(request);
        }

    }

}
