FROM ubuntu:21.10

RUN mkdir -p /usr/src/jiraParser/
WORKDIR /usr/src/jiraParser/

COPY . /usr/src/jiraParser/

CMD ["/bin/bash"]
