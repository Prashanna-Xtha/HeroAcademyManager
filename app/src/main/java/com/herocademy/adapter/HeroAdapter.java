package com.herocademy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.herocademy.R;
import com.herocademy.model.Hero;

import java.util.ArrayList;
import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> {

    private List<Hero> heroList;
    private final List<Hero> selectedHeroes;
    private final boolean showCheckboxes;

    public HeroAdapter(List<Hero> heroList, boolean showCheckboxes) {
        this.heroList = new ArrayList<>(heroList);
        this.selectedHeroes = new ArrayList<>();
        this.showCheckboxes = showCheckboxes;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hero, parent, false);
        return new HeroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        Hero hero = heroList.get(position);

        holder.tvName.setText(hero.getName() + " [" + hero.getHeroType() + "]");
        holder.tvEnergy.setText("HP: " + hero.getEnergy() + "/" + hero.getMaxEnergy());
        holder.tvXP.setText("XP: " + hero.getExperience() + "  |  Skill: " + (hero.getSkill() + hero.getExperience()) + "  |  Res: " + hero.getResilience());

        holder.checkbox.setVisibility(showCheckboxes ? View.VISIBLE : View.GONE);
        holder.checkbox.setChecked(selectedHeroes.contains(hero));

        // Clicking the row or the checkbox both toggle selection
        View.OnClickListener toggleSelection = v -> {
            if (selectedHeroes.contains(hero)) {
                selectedHeroes.remove(hero);
            } else {
                selectedHeroes.add(hero);
            }
            notifyItemChanged(position);
        };

        holder.checkbox.setOnClickListener(toggleSelection);
        holder.itemView.setOnClickListener(toggleSelection);
    }

    @Override
    public int getItemCount() { return heroList.size(); }

    public List<Hero> getSelectedHeroes() { return selectedHeroes; }

    public void updateList(List<Hero> newList) {
        heroList = new ArrayList<>(newList);
        selectedHeroes.clear();
        notifyDataSetChanged();
    }

    static class HeroViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        TextView tvName, tvEnergy, tvXP;

        HeroViewHolder(View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkboxHero);
            tvName = itemView.findViewById(R.id.tvHeroName);
            tvEnergy = itemView.findViewById(R.id.tvHeroEnergy);
            tvXP = itemView.findViewById(R.id.tvHeroXP);
        }
    }
}