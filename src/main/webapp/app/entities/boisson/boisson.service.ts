import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBoisson } from 'app/shared/model/boisson.model';

type EntityResponseType = HttpResponse<IBoisson>;
type EntityArrayResponseType = HttpResponse<IBoisson[]>;

@Injectable({ providedIn: 'root' })
export class BoissonService {
  public resourceUrl = SERVER_API_URL + 'api/boissons';

  constructor(protected http: HttpClient) {}

  create(boisson: IBoisson): Observable<EntityResponseType> {
    return this.http.post<IBoisson>(this.resourceUrl, boisson, { observe: 'response' });
  }

  update(boisson: IBoisson): Observable<EntityResponseType> {
    return this.http.put<IBoisson>(this.resourceUrl, boisson, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBoisson>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBoisson[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
