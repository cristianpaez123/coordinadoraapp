package com.example.coordinadoraapp.ui.mainActivity.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coordinadoraapp.databinding.ItemLocationBinding;
import com.example.coordinadoraapp.ui.model.LocationUi;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<LocationUi> items = new ArrayList<>();

    public void updateItems(List<LocationUi> newItems) {
        this.items = newItems;
        notifyDataSetChanged(); // puedes usar DiffUtil si deseas mejor rendimiento
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemLocationBinding binding = ItemLocationBinding.inflate(inflater, parent, false);
        return new LocationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        private final ItemLocationBinding binding;

        public LocationViewHolder(ItemLocationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LocationUi item) {
            binding.textLabel.setText("etiqueta1d: " + item.label);
            binding.textObservation.setText("Observación: " + item.observation);
            // Puedes cargar íconos si lo necesitas (ej: Glide o directamente desde drawable)
        }
    }
}
