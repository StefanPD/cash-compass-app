# Cash compass app

## Kubernetes configuration

### Prerequisites

* Running kubernetes cluster. Use [Orbstack](https://orbstack.dev/) or [kind](https://kind.sigs.k8s.io/)
* Helm
* [Kompose](https://kompose.io/): a conversion tool for Docker Compose to container orchestrators such as Kubernetes (or OpenShift)

We need to push docker image to a registry. If you don't want to use docker hub or similar public registries you can create 
a private one. Check this [instructions](https://pkuwwt.github.io/techniques/2020-04-04-setup-a-private-docker-registry/)

### Install kube-prometheus-stack

* Add helm repository: `helm repo add prometheus-community https://prometheus-community.github.io/helm-charts` 
* Update repository: `helm repo update`
* Install kube-prometheus-stack from `kubernetes/helm/kube-prometheus-stack` directory: `helm upgrade -f values.yml kube-prometheus-stack --create-namespace --namespace kube-prometheus-stack prometheus-community/kube-prometheus-stack`

### Convert docker-compose to kubernetes resources

Run: `kompose convert` in the root directory (where docker-compose.yaml file is).

> NOTE: I moved the generated files to `kubernetes/resources` directory manually

#### Apply resources to your cluster

Edit generated kompose resources:

1. In `app-service.yaml` add annotations
    ```yaml
        prometheus.io/scrape: "true"
        prometheus.io/path: "/actuator/prometheus"
    ```
2. In `app-deployment.yaml` update `image: 192.168.2.80:5000/jzoric/cash-compass-app:2` to use your image

### Kustomize or Helm or jsonnet or ...

For this first version, we can use our simple resource yaml files that we can apply to the cluster. 

In the future versions we will explore other options like Kustomize, Helm, jsonnet etc. 


### Monitoring

TODO:
1. Document Prometheus scrape config 
2. Check: Grafana dashboards
3. Document: Port forward for monitoring stack (Grafana and Prometheus)
4. 