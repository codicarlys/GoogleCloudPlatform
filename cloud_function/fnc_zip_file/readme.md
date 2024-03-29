# Cloud function
Esta cloud function tem como objetivo zipar multiplos arquivos csvs em um arquivo zip

## Indice
[Deploy Function](#deploy)
[Build Image](#build-docker)
[Utilidade](#util)


Input: 
pytest - Covarage

## Deploy
gcloud functions deploy fnc_zip_file_128mb \
--region=us-central1 \
--memory=128 \
--runtime=python310 \
--timeout=540 \
--source=. \
--entry-point=main \
--trigger-http

## Util
<!-- ===============================
----------UTIL COMMANDS
=============================== -->
functions-framework --target main --debug

# TEST
curl --data '{"gcs_input": "<bucket>/<input_path>/github_repos_files","gcs_output": "<bucket><output>", "filename":"github_repos_files"}'  \
  --header "Content-Type: application/json" \
  --header "Accept: application/json" <url> &

{
  "gcs_input":  "<bucket>/<input_file>",
  "gcs_output": "<bucket>/<out_path>",
}

## Testes
Testes abaixo foram feitos executando o seguinte processo. Baixar os arquivos acumulá-los e subir para núvem

1. Arquivos: (students_1.csv , students_2.csv) 12 Mb => 24MB cada 
   - Resultado: 
     - Cloud Function 128MB -> Ok

2. Arquivo: students.csv 51 MB
  - Resultado: 
    - Cloud Function 128MB -> Nok
    - Cloud Function 256MB -> Ok
    - Cloud Function 512MB -> Ok

3. Arquivos: (students_1.csv, students_2.csv ) 51 MB * 2 => 102 MB
    - Cloud Function 128MB -> Nok
    - Cloud Function 256MB -> Ok
    - Cloud Function 512MB -> Ok

...
7. Arquivos: (tabela_dummy.csv) 51 MB * 7 => 357 MB
    - Cloud Function 128MB -> Nok
    - Cloud Function 512MB -> Nok

### Stream Test
Estes testes foram feitos baixando o arquivo em partes e em seguida subindo essa parte para um único destino

8. Arquivo arquivo6gb => 6gb
  - Cloud Function 1gb -> Ok


### Build Docker
[Indice](#indice)
Este comando é responsável por gerar uma imagem docker para cloud function
```shell
gcloud builds submit --pack image=us-central1-docker.pkg.dev/<image_path>,env=GOOGLE_FUNCTION_TARGET=main
```

### Testes
[Indice](#indice)
Arquivos utilizados para efetuar o stream 
