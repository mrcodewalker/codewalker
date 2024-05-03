import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../enviroments/enviroment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class RankingService{
  private apiGetRanking = `${environment.apiBaseUrl}/ranking/collect`;
  private apiGetRankingTop1 = `${environment.apiBaseUrl}/ranking/top/1`;
  private apiGetRankingTop2 = `${environment.apiBaseUrl}/ranking/top/2`;
  private apiGetRankingTop3 = `${environment.apiBaseUrl}/ranking/top/3`;

  constructor(private http: HttpClient) {
  }
  getRanking(student_code: string): Observable<any>{
    const params = new HttpParams().set('student_code', student_code);
    return this.http.get<any>(this.apiGetRanking, { params });
  }
  getRankingTop1() :Observable<any>{
    return this.http.get<any>(this.apiGetRankingTop1);
  }
  getRankingTop2() :Observable<any>{
    return this.http.get<any>(this.apiGetRankingTop2);
  }
  getRankingTop3() :Observable<any>{
    return this.http.get<any>(this.apiGetRankingTop3);
  }
}
