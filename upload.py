import requests

def upload_file(filepath):
    # Ensure the URL is correct
    url = 'http://127.0.0.1:8080/files'

    # Open the file in binary mode
    with open(filepath, 'rb') as f:
        # Create a dictionary with the file to be uploaded
        files = {'file': (filepath, f)}

        # Make the POST request
        response = requests.post(url, files=files)

        # Print the server's response
        print(response.text)

if __name__ == "__main__":
    # Sample file path; replace with your desired file path
    file_path = "testfile.txt"
    upload_file(file_path)
