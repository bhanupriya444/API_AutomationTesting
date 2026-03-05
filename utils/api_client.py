import requests
from config import WORKFLOW_API_ROOT, DATA_API_ROOT
from utils.logger import logger

class APIClient:
    """Base API Client for making requests to Bubble.io APIs"""
    
    def __init__(self, api_root_url):
        self.api_root_url = api_root_url
        self.session = requests.Session()
    
    def make_request(self, endpoint, method='GET', params=None, headers=None, data=None, json_data=None):
        """
        Make an HTTP request to the API
        
        Args:
            endpoint: API endpoint path
            method: HTTP method (GET, POST, PUT, DELETE, etc.)
            params: Query parameters
            headers: Custom headers
            data: Form data
            json_data: JSON body data
        
        Returns:
            Response object
        """
        url = f"{self.api_root_url}/{endpoint}"
        
        try:
            response = self.session.request(
                method=method,
                url=url,
                params=params,
                headers=headers,
                data=data,
                json=json_data
            )
            response.raise_for_status()
            logger.info(f"{method} {url} - Status: {response.status_code}")
            return response
        except requests.exceptions.RequestException as e:
            logger.error(f"Request failed for {url}: {str(e)}")
            raise


class WorkflowAPIClient(APIClient):
    """Client for Workflow API"""
    
    def __init__(self):
        super().__init__(WORKFLOW_API_ROOT)
    
    def call_workflow(self, workflow_name, params=None, data=None):
        """
        Call a specific workflow
        
        Args:
            workflow_name: Name of the workflow to call
            params: Query parameters
            data: Workflow parameters as JSON
        
        Returns:
            JSON response
        """
        return self.make_request(
            endpoint=workflow_name,
            method='POST',
            params=params,
            json_data=data
        ).json()


class DataAPIClient(APIClient):
    """Client for Data API"""
    
    def __init__(self):
        super().__init__(DATA_API_ROOT)
    
    def get_data(self, endpoint, params=None):
        """
        GET request to data API
        
        Args:
            endpoint: Data endpoint
            params: Query parameters
        
        Returns:
            JSON response
        """
        return self.make_request(
            endpoint=endpoint,
            method='GET',
            params=params
        ).json()
    
    def create_data(self, endpoint, data):
        """
        POST request to data API
        
        Args:
            endpoint: Data endpoint
            data: Data to create
        
        Returns:
            JSON response
        """
        return self.make_request(
            endpoint=endpoint,
            method='POST',
            json_data=data
        ).json()
    
    def update_data(self, endpoint, data):
        """
        PATCH request to data API
        
        Args:
            endpoint: Data endpoint
            data: Data to update
        
        Returns:
            JSON response
        """
        return self.make_request(
            endpoint=endpoint,
            method='PATCH',
            json_data=data
        ).json()
    
    def delete_data(self, endpoint):
        """
        DELETE request to data API
        
        Args:
            endpoint: Data endpoint
        
        Returns:
            JSON response
        """
        return self.make_request(
            endpoint=endpoint,
            method='DELETE'
        ).json()