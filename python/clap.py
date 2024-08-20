from msclap import CLAP

# Load model (Choose between versions '2022' or '2023')
# The model weight will be downloaded automatically if `model_fp` is not specified
clap_model = CLAP(version = '2023', use_cuda=True)

testText = ["An heavy metal song."]
filepath = ['C:\\Users\\lorda\\Downloads\\BEAST_IN_BLACK-PowerOfTheBeast.mp3']

# Extract text embeddings
text_embeddings = clap_model.get_text_embeddings(testText)
print(text_embeddings)
# Extract audio embeddings
audio_embeddings = clap_model.get_audio_embeddings(filepath)
print(audio_embeddings)
# Compute similarity between audio and text embeddings 
similarities = clap_model.compute_similarity(audio_embeddings, text_embeddings)

print(f"{similarities}")
print("Trying out captioning")
caption_model = CLAP(version = 'clapcap', use_cuda=True)

audio_files = ['C:\\Users\\lorda\\Downloads\\FROSTBITE_ORCKINGS-Bye_Bye_Wintertime.mp3',
               'C:\\Users\\lorda\\Downloads\\Beethoven-FurElise.mp3',
               'C:\\Users\\lorda\\Downloads\\Eminem-Houdini.mp3',
               'C:\\Users\\lorda\\Downloads\\BEAST_IN_BLACK-PowerOfTheBeast.mp3']

captions = caption_model.generate_caption(audio_files, resample=True, beam_size=5, entry_length=67, temperature=0.7)

for i in range(len(audio_files)):
    print(f"Audio file: {audio_files[i]} \n")
    print(f"Generated caption: {captions[i]} \n")