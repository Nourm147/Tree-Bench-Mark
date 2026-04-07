import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns
import re
import os

def process_benchmark_csv(file_path):
    if not os.path.exists(file_path):
        print(f"Error: {file_path} not found.")
        return
        
    # Load CSV
    df = pd.read_csv(file_path)
    
    # 1. Rename distributions to "NSorted X%" or "Random"
    def format_dist(d):
        if "NEARLY_SORTED" in d:
            percent = d.replace("NEARLY_SORTED", "")
            return f"NSorted {percent}%"
        return d.capitalize()
    
    df['Dist'] = df['distribution'].apply(format_dist)
    
    # 2. Extract Tree type (BST/RBT/Quick Sort) and Metric from labels
    def split_label(label):
        if "Quick Sort" in label:
            return "Quick Sort", "Sorting Time"
        parts = label.split(' ')
        return parts[0], f"{parts[1]} Time"
        
    df[['Tree', 'Metric']] = df['label'].apply(lambda x: pd.Series(split_label(x)))

    # 3. Create a unified dataframe for plotting
    # Capture Mean Times
    plot_data = df[['Dist', 'Tree', 'Metric', 'mean_ms']].rename(columns={'mean_ms': 'Value'})
    
    # Capture Heights (only for BST/RBT rows)
    heights = df[df['Tree'].isin(['BST', 'RBT'])].drop_duplicates(['Dist', 'Tree'])
    heights = heights[['Dist', 'Tree', 'tree_height']].rename(columns={'tree_height': 'Value'})
    heights['Metric'] = 'Node Height'
    
    final_df = pd.concat([plot_data, heights], ignore_index=True)

    # --- Plotting 5 Separate Images ---
    sns.set_theme(style="whitegrid")
    report_metrics = ['Node Height', 'Insert Time', 'Contains Time', 'Delete Time', 'Sorting Time']
    
    for metric in report_metrics:
        plt.figure(figsize=(10, 8))
        subset = final_df[final_df['Metric'] == metric]
        
        # Sort Distributions correctly (Random, 1%, 5%, 10%)
        subset['sort_key'] = subset['Dist'].apply(lambda x: -1 if x == 'Random' else int(re.search(r'\d+', x).group()))
        subset = subset.sort_values('sort_key')

        # Generate Bar Plot
        sns.barplot(data=subset, x='Dist', y='Value', hue='Tree', palette="viridis")
        
        # Formatting
        plt.title(f"{metric} Comparison", fontsize=20, fontweight='bold', pad=35)
        plt.xlabel("Input Distribution", fontsize=14, labelpad=15)
        plt.ylabel("Node Count" if "Height" in metric else "Mean Time (ms)", fontsize=14)

        # Log scale for massive disparities
        if any(m in metric for m in ["Height", "Contains", "Sorting"]):
            plt.yscale("log")
            plt.ylabel(f"{plt.gca().get_ylabel()} (Log Scale)")
            plt.grid(True, which="both", ls="-", alpha=0.3)

        plt.legend(title="Type", fontsize=12, title_fontsize=13)
        plt.tight_layout(pad=3.0)
        
        # Save file
        filename = f"{metric.lower().replace(' ', '_')}_comparison.png"
        plt.savefig(filename, dpi=300, bbox_inches='tight')
        print(f"Generated: {filename}")
        plt.close()

# Run the script
process_benchmark_csv('results/benchmark_results.csv')